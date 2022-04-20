package com.sungjin.reviewroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sungjin.reviewroom.service.ReviewService;
import com.sungjin.reviewroom.dto.AddReviewPayload;

@RestController
@RequestMapping("api/review")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/review")
    public int addReview(@RequestBody AddReviewPayload addReviewPayload) {
       reviewService.addNewReview(addReviewPayload);
       return 0;
    }
}
