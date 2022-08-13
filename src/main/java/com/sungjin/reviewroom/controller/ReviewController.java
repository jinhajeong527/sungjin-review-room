package com.sungjin.reviewroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sungjin.reviewroom.service.ReviewService;
import com.sungjin.reviewroom.security.UserDetailsImpl;


import com.sungjin.reviewroom.dto.AddReviewPayload;
import com.sungjin.reviewroom.dto.MessageResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('REVIEWER')")
    public ResponseEntity<?> addReview(@RequestBody AddReviewPayload addReviewPayload) {

        UserDetailsImpl userDetails =
	                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getEmail();
        reviewService.addNewReview(addReviewPayload, userEmail);
        return ResponseEntity
                .ok()
                .body(new MessageResponse("Review you just wrote registered successfully!"));
    }
}
