package com.sungjin.reviewroom.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Show;




public interface ShowRepository extends JpaRepository<Show, Integer>  {
    Set<Show> findAllByGenresInAndReviewsNotInOrderByLatelyReviewedDateDesc(Set<Genre> list, List<Review> reviews);
}
