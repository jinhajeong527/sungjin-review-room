package com.sungjin.reviewroom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sungjin.reviewroom.dto.MessageResponse;
import com.sungjin.reviewroom.dto.WishListPayLoad;
import com.sungjin.reviewroom.exception.ShowNotPresentException;
import com.sungjin.reviewroom.exception.WishlistAlreadyExistException;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ReviewerService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/reviewer")
public class ReviewerController {

    @Autowired
    ReviewerService reviewerService;
    
    @PostMapping("/wishlist")
    public ResponseEntity<?> addToWishList(@RequestBody WishListPayLoad wishListPayLoad, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        int result;
        try {
            result = reviewerService.addToWishlist(wishListPayLoad.getShowId(), userDetails.getEmail());
        } catch(WishlistAlreadyExistException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("This show already exists in reviewer's wishlist"));
        } catch(ShowNotPresentException exception) {
            return ResponseEntity.badRequest().body(new MessageResponse("This show cannot be added to wishlist cause it is never been reviewed before on our service"));
        }
        if(result == 1) return ResponseEntity.ok(new MessageResponse("Wishlist added successfully!"));
        else return ResponseEntity.badRequest().body(new MessageResponse("Your attempt to add to wishlist has failed"));
    }
    @DeleteMapping("/wishlist")
    public ResponseEntity<?> deleteWishList(@RequestBody WishListPayLoad wishListPayLoad, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        int result;
        result = reviewerService.deleteWishlist(wishListPayLoad.getShowId(), userDetails.getEmail());
        return ResponseEntity.ok().body(new MessageResponse("The show added to your wishlist has been deleted"));
    }



    
}
