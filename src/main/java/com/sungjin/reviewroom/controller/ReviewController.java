package com.sungjin.reviewroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sungjin.reviewroom.service.ReviewService;
import com.sungjin.reviewroom.security.UserDetailsImpl;


import com.sungjin.reviewroom.dto.AddReviewPayload;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('REVIEWER')")
    public int addReview(@RequestBody AddReviewPayload addReviewPayload, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();
        reviewService.addNewReview(addReviewPayload, userEmail);
        return 0;
    }
}
