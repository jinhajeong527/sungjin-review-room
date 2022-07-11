package com.sungjin.reviewroom.controller;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ShowService;

import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/show")
public class ShowController {
    
    @Autowired
    ShowService showService;
    
    @GetMapping("/preferred")
    @PreAuthorize("hasRole('REVIEWER')")
    public Set<Show> getLatestPrefrredShows(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();
        Set<Show> show = showService.getLatestPrefrredShows(userEmail);
        return show;
    }

    @GetMapping("/mostReviewed")
    public Page<Show> getTheMostReviewedShows(@RequestBody PaginationPayload paginationPayload) {
        int pageSize = paginationPayload.getPageSize();
        int pageNumber = paginationPayload.getPageNumber();
        Page<Show> mostReviewedShows = showService.getTheMostReviewedShows(pageNumber, pageSize);
        System.out.println(mostReviewedShows.getContent());
        return mostReviewedShows;
    }

    @GetMapping("/wishlist")
    public Set<Show> getShowsAddedToWishlist(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userEmail = userDetails.getEmail();
        Set<Show> wishlistShows = showService.getShowsAddedToWishlist(userEmail);
        return wishlistShows;
    }
}
