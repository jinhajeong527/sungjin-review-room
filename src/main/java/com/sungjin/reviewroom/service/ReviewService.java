package com.sungjin.reviewroom.service;

import com.sungjin.reviewroom.dto.AddReviewPayload;

public interface ReviewService {
    public void addNewReview(AddReviewPayload addReviewPayload, String email);
}
