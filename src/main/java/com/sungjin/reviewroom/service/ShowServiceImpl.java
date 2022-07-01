package com.sungjin.reviewroom.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.dao.ReviewRepository;
import com.sungjin.reviewroom.dao.ReviewerRepository;
import com.sungjin.reviewroom.dao.ShowRepository;
import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.Show;

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
   
    @Override
    @Transactional
    public Set<Show> getLatestPrefrredShows(String email) {
        // 이메일 정보로 리뷰어 정보 얻어오기
        Reviewer reviewer = reviewerRepository.getByEmail(email);
        // 해당 리뷰어의 작성 리뷰를 가져온다. 쇼 리스트 조건으로 (NotIn) 넣어줄 것이기 때문이다.
        List<Review> reviews = reviewRepository.findByReviewer(reviewer);
        // 해당 리뷰어 선호 장르 Set 가져오기
        Set<Genre> genres = reviewer.getGenres();
        // 장르 선호가 16,18인 리뷰어가 장르가 16, 18인 쇼를 가져온다면 리스트로 받아올 시에 중복될 수 있기 때문에 
        // Set<Show>로 받아서 중복을 제거했다.
        Set<Show> set = showRepository.findAllByGenresInAndReviewsNotInOrderByLatelyReviewedDateDesc(genres, reviews);
        
        return set;
    }

    @Override
    @Transactional
    public Page<Show> getTheMostReviewedShows() {
        Calendar calendar = Calendar.getInstance();
        // 지난 달 1일 설정하기
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        Date firstDateOfLastMonth = calendar.getTime();
        // 지난 달 마지막 날짜 설정하기
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDateOfLstMonth = calendar.getTime();
        Pageable pageable = PageRequest.of(0, 10, Sort.by( Sort.Direction.DESC, "reviewsCount" ));
        
        return showRepository.findAllWithReviewsCount(pageable, firstDateOfLastMonth, lastDateOfLstMonth);
    }

    @Override
    @Transactional
    public Set<Show> getShowsAddedToWishlist(String email) {
        Reviewer reviewer = reviewerRepository.getByEmail(email);
        Set<Show> show = reviewer.getShows();
        return show;
    }
    
}
