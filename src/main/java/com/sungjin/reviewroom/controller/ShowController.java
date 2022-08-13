package com.sungjin.reviewroom.controller;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ShowService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/show")
public class ShowController {
    
    @Autowired
    ShowService showService;
    
    @GetMapping("/preferred")
    @PreAuthorize("hasRole('REVIEWER')")
    public List<Show> getLatestPrefrredShows(@RequestParam int pageNumber, @RequestParam int pageSize, Authentication authentication) {
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Show> show = showService.getLatestPrefrredShows(userDetails.getEmail(), paginationPayload);
        return show;
    }

    @GetMapping("/mostReviewed")
    public List<ShowResponsePayload> getTheMostReviewedShows(@RequestParam int pageNumber, @RequestParam int pageSize) {
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        List<ShowResponsePayload> mostReviewedShows = showService.getTheMostReviewedShows(paginationPayload);  
        return mostReviewedShows;
    }

    @GetMapping("/wishlist")
    @PreAuthorize("hasRole('REVIEWER')")
    public List<Wishlist> getShowsAddedToWishlist(@RequestParam int pageNumber, @RequestParam int pageSize, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        List<Wishlist> wishlistShows = showService.getShowsAddedToWishlist(userDetails.getEmail(), paginationPayload);
        return wishlistShows;
    }
}
