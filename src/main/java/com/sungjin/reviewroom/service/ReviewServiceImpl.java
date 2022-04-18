package com.sungjin.reviewroom.service;

import com.sungjin.reviewroom.dao.ReviewRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.ShowRepository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.dto.AddReviewPayload;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Genre;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewerRepository reviewerRepository;
    ReviewRepository reviewRepository;
    ShowRepository showRepository;
    GenreRepository genreRepository;

    @Autowired
    public ReviewServiceImpl(ReviewerRepository reviewerRepository, ReviewRepository reviewRepository, ShowRepository showRepository,GenreRepository genreRepository) {
        this.reviewerRepository = reviewerRepository;
        this.reviewRepository = reviewRepository;
        this.showRepository = showRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional
    public void addNewReview(AddReviewPayload addReviewPayload) {
        //임시 user 정보 하드코딩: JWT 적용하면서 수정할 것
        int reviewerId = 1;
        Reviewer reviewer = reviewerRepository.getById(reviewerId);

        //AddReviewPayload에서 showId 받아온다.
        int showId = addReviewPayload.getShow().getId();
        Show receivedShow = addReviewPayload.getShow();
       
        //Review 얻어오기
        Review review = addReviewPayload.getReview();
        reviewer.add(review);
     
        //DB에 저장된 show인지 확인한다.
        Optional<Show> isExistingShow = showRepository.findById(showId);
       
        //새로 등록하는 Show일 경우에 Genre 등록해주기 
        if(!isExistingShow.isPresent()) {
            //해당 show에 genre도 등록한다.
            List<Genre> genres = addReviewPayload.getGenres();
            for(Genre genre : genres) {
                Genre genreFromDb = genreRepository.getById(genre.getId());
                receivedShow.addGenre(genreFromDb);
            }
        }else { //이미 디비에 등록된 Show일 경우
            receivedShow = showRepository.getById(showId);
        }
        receivedShow.addReview(review);
        reviewRepository.save(review);
      
    }
    
}
