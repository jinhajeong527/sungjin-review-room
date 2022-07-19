package com.sungjin.reviewroom.service;

import com.sungjin.reviewroom.dto.SignupPayload;
import com.sungjin.reviewroom.entity.Reviewer;
import com.sungjin.reviewroom.entity.VerificationToken;

public interface ReviewerService {
    void createVerificationTokenForReviewer(Reviewer user, String token);
    VerificationToken generateVerificationTokenAgain(String existingVerificationToken);
    VerificationToken getVerificationToken(String VerificationToken);
    Reviewer registerUser(SignupPayload signupPayload);
    void saveSignedUpReviewer(Reviewer reviewer);
    Reviewer getReviewer(String token);
    int addToWishlist(int showId, String reviewerEmail);
    String findTokenWithLoginInfo(String email);
    int deleteWishlist(int showId, String email);
}
