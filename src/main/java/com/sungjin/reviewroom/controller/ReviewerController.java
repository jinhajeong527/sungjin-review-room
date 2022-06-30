package com.sungjin.reviewroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sungjin.reviewroom.dto.MessageResponse;
import com.sungjin.reviewroom.dto.WishListPayLoad;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ReviewerService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reviewer")
public class ReviewerController {

    @Autowired
    ReviewerService reviewerService;
    
    @PostMapping("/wishlist")
    public ResponseEntity<?> addToWishList(@RequestBody WishListPayLoad wishListPayLoad, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();
        int showId = wishListPayLoad.getShowId();
        int result = reviewerService.addToWishList(showId, userEmail);
        if(result == 1) return ResponseEntity.ok(new MessageResponse("Wishlist added successfully!"));
        else return ResponseEntity.badRequest().body(new MessageResponse("Your attempt to add to wishlist has failed"));
    }



    
}
