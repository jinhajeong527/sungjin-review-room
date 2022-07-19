package com.sungjin.reviewroom.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;
import com.sungjin.reviewroom.entity.Show;




public interface ShowRepository extends JpaRepository<Show, Integer>  {

    Page<Show> findDistinctByGenresInAndReviewsNotInOrderByLatelyReviewedDateDesc(Set<Genre> list, List<Review> reviews, Pageable pageable);

    // @Query(value = "SELECT s, COUNT(r) AS reviewsCount"+
    //                " FROM Show s JOIN s.reviews r" + 
    //                " WHERE s.latelyReviewedDate BETWEEN :firstDateOfLastMonth AND :lastDateOfLastMonth" +
    //                " GROUP BY s ")
    // Page<Object> findAllWithReviewsCount(Pageable pageable, @Param("firstDateOfLastMonth") Date startdate, @Param("lastDateOfLastMonth") Date endDate);
}
