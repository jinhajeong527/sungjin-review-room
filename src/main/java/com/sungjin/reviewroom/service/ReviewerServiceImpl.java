package com.sungjin.reviewroom.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.RoleRepository;
import com.sungjin.reviewroom.dao.ShowRepository;
import com.sungjin.reviewroom.dao.VerificationTokenRepository;
import com.sungjin.reviewroom.dao.WishlistRepository;
import com.sungjin.reviewroom.dto.SignupPayload;
import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Role;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.VerificationToken;
import com.sungjin.reviewroom.entity.Wishlist;
import com.sungjin.reviewroom.exception.ReviewerAlreadyExistException;
import com.sungjin.reviewroom.exception.ShowNotPresentException;
import com.sungjin.reviewroom.exception.WishlistAlreadyExistException;
import com.sungjin.reviewroom.model.EnumRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ReviewerServiceImpl implements ReviewerService {
    
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ShowRepository showRepository;
    @Autowired
    VerificationTokenRepository verificationTokenRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    PasswordEncoder encoder;

    @Override
    @Transactional
    public Reviewer registerUser(SignupPayload signupPayload) {
        //이미 존재하는 이메일인지 확인한다.
        if (reviewerRepository.existsByEmail(signupPayload.getEmail())) {
            throw new ReviewerAlreadyExistException("해당 이메일 정보로 등록된 유저가 이미 있습니다: " + signupPayload.getEmail());
		}

        Reviewer reviewer = new Reviewer(signupPayload.getEmail(), 
                                         encoder.encode(signupPayload.getPassword()), 
                                         signupPayload.getFirstName(), 
                                         signupPayload.getLastName(), 
                                         signupPayload.getMbti()                  
                                        );

        // signupPayload에서 얻어온 Role 정보를 통해 DB에서 Role 엔티티 얻어와 Reviewer에 추가해주기     
        Set<String> rolesStr = signupPayload.getRole();
        Set<Role> roles = new HashSet<>();
        if(rolesStr == null) {
            Role userRole = roleRepository.findByName(EnumRole.ROLE_REVIEWER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            rolesStr.forEach(role -> {
                switch(role) {
                case "admin":
                    Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(EnumRole.ROLE_REVIEWER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        // signupPayload에서 얻어온 Genre 정보를 통해 DB에서 Genre 엔티티 얻어와 Reviewer에 추가해주기
        Set<Genre> genres = signupPayload.getGenres();
        Iterator<Genre> genreIterator = genres.iterator();
        Set<Genre> genresFromDb = new HashSet<>();

        while(genreIterator.hasNext()) {
            int genreId = genreIterator.next().getId();
            Genre genre = genreRepository.getById(genreId);
            genresFromDb.add(genre);
        }

        reviewer.setGenres(genres); //reviewer_genre 조인테이블에 데이터 추가될 것
        reviewer.setRoles(roles); //reviewer_role 조인테이블에 데이터 추가될 것.
        reviewer.setVerified(false);
       
        return  reviewerRepository.save(reviewer);
    }

    @Override
    @Transactional
    public void createVerificationTokenForReviewer(Reviewer reviewer, String token) {
        final VerificationToken myToken = new VerificationToken(token, reviewer);
        verificationTokenRepository.save(myToken);
    }

    @Override
    @Transactional
    public VerificationToken generateVerificationTokenAgain(String existingVerificationToken) {
        VerificationToken expiredVerificationToken = verificationTokenRepository.findByToken(existingVerificationToken);
        expiredVerificationToken.updateToken(UUID.randomUUID().toString());
        expiredVerificationToken = verificationTokenRepository.save(expiredVerificationToken);
        return expiredVerificationToken;
    }

    @Override
    @Transactional
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }
    @Override
    @Transactional
    public void saveSignedUpReviewer(final Reviewer reviewer) {
        reviewerRepository.save(reviewer);
    }

    @Override
    @Transactional
    public Reviewer getReviewer(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        Reviewer reviewer = reviewerRepository.getById(verificationToken.getReviewer().getId());
        return reviewer;
    }

    @Override
    @Transactional
    public int addToWishlist(int showId, String reviewerEmail) {
        Reviewer reviewer = reviewerRepository.getByEmail(reviewerEmail);
        Set<Wishlist> wishlist = reviewer.getWishlist();
       
        // 리뷰 등록된 적 없는 쇼는 위시리스트 등록할 수 없다.
        Optional<Show> theShowOptional = showRepository.findById(showId);
        if(!theShowOptional.isPresent()) 
            throw new ShowNotPresentException("This show cannot be added to wishlist cause it is never been reviewed before on our service");
        // 이미 위시리스트에 등록되어 있을 경우엔 등록할 수 없다.
        if(wishlist != null)
            for(Wishlist wish : wishlist) 
                if(wish.getShow().getId() == showId) 
                    throw new WishlistAlreadyExistException("This show already exists in reviewer's wishlist");
        
        Wishlist newWishlist = new Wishlist(theShowOptional.get(), reviewer);
        wishlist.add(newWishlist);
        reviewer.setWishlist(wishlist);
        // Cascade.ALL에 의해 리뷰어 엔티티 저장되면서 위시리스트 엔티티도 같이 저장될 것이다.
        Reviewer updatedReviewer = reviewerRepository.save(reviewer);
        if(updatedReviewer != null) return 1;
        else return 0;
    }

    @Override
    @Transactional
    public int deleteWishlist(int showId, String reviewerEmail) {
        Reviewer reviewer = reviewerRepository.getByEmail(reviewerEmail);
        Set<Wishlist> wishlist = wishlistRepository.getByReviewer(reviewer);
        if(wishlist != null)
            for(Wishlist wish : wishlist) 
                if(wish.getShow().getId() == showId) {
                    wishlistRepository.delete(wish);
                    break;
                }
        return 1;
    }

    // 회원가입 인증 하지 않은 리뷰어가 로그인 시도할 때, 토큰 만료 됐을 경우 재인증 시도 페이지로 이동시키는데, 이 때 기존 생성되었던 토큰 데이터가 필요해서 작성한 메서드
    @Override
    @Transactional
    public String findTokenWithLoginInfo(String email) {
        Reviewer reviewer = reviewerRepository.getByEmail(email);
        VerificationToken verificationToken = verificationTokenRepository.findByReviewer(reviewer);
        String token = verificationToken.getToken();
        return token;
    }
}
