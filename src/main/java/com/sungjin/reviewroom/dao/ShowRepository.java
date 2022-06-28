package com.sungjin.reviewroom.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Show;




public interface ShowRepository extends JpaRepository<Show, Integer>  {

    Set<Show> findAllByGenresInAndReviewsNotInOrderByLatelyReviewedDateDesc(Set<Genre> list, List<Review> reviews);

    @Query(value = "select s, count(r) as reviewsCount from Show s join s.reviews r group by s", countQuery = "select count(s) from Show s")
    Page<Show> findAllWithReviewsCount(PageRequest pageRequest);
}
