package com.sungjin.reviewroom.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.dao.ReviewRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.ShowRepository;
import com.sungjin.reviewroom.dao.WishlistRepository;
import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;

@Service
public class ShowServiceImpl implements ShowService {

    @Autowired
    ShowRepository showRepository;
    @Autowired
    ReviewerRepository reviewerRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    WishlistRepository wishlistRepository;
   
    Pageable pageable;

    @Autowired
    EntityManager entityManager;


    @Override
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Override
    @Transactional
    public List<Show> getLatestPrefrredShows(String email, PaginationPayload paginationPayload) {
        // 이메일 정보로 리뷰어 정보 얻어오기
        Reviewer reviewer = reviewerRepository.getByEmail(email);
        // 해당 리뷰어의 작성 리뷰를 가져온다. 쇼 리스트 조건으로 (NotIn) 넣어줄 것이기 때문이다.
        List<Review> reviews = reviewRepository.findByReviewer(reviewer);
        // 해당 리뷰어 선호 장르 Set 가져오기
        Set<Genre> genres = reviewer.getGenres();
        pageable = PageRequest.of(paginationPayload.getPageNumber(), paginationPayload.getPageSize()); 
        // 장르 선호가 16,18인 리뷰어가 장르가 16, 18인 쇼를 가져온다면 리스트로 받아올 시에 중복될 수 있기 때문에 Distinct 추가했다.
        Page<Show> list = showRepository.findDistinctByGenresInAndReviewsNotInOrderByLatelyReviewedDateDesc(genres, reviews, pageable);
        return list.getContent();
    }

    @Override
    @Transactional
    public List<ShowResponsePayload> getTheMostReviewedShows(PaginationPayload paginationPayload) {
        Calendar calendar = Calendar.getInstance();
        // 지난 달의 1일 설정하기
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        Date firstDateOfLastMonth = calendar.getTime();
        // 지난 달의 마지막 날짜 설정하기
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfLstMonth = calendar.getTime();

        Query query = entityManager.createQuery("SELECT new com.sungjin.reviewroom.dto.ShowResponsePayload(s, COUNT(r) AS reviewsCount) " + 
                                                "FROM Show s JOIN s.reviews r " + 
                                                "WHERE s.latelyReviewedDate BETWEEN :firstDateOfLastMonth AND :lastDateOfLastMonth " +
                                                "GROUP BY s " +
                                                "ORDER BY reviewsCount DESC");
        query.setParameter("firstDateOfLastMonth", firstDateOfLastMonth);
        query.setParameter("lastDateOfLastMonth", lastDateOfLstMonth);
        query.setFirstResult(paginationPayload.getPageNumber());
        query.setMaxResults(paginationPayload.getPageSize());
        List<ShowResponsePayload> list = query.getResultList();
        return list;
    }

    @Override
    @Transactional
    public List<Wishlist> getShowsAddedToWishlist(String email, PaginationPayload paginationPayload) {
        Reviewer reviewer = reviewerRepository.getByEmail(email);
        pageable = PageRequest.of(paginationPayload.getPageNumber(), paginationPayload.getPageSize()); 
        Page<Wishlist> list = wishlistRepository.findAllByReviewerOrderByCreatedDateDesc(reviewer, pageable);
        return list.getContent();
    }
    
}
