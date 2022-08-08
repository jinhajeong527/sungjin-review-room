package com.sungjin.reviewroom.controller;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ShowService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/show")
public class ShowController {
    
    @Autowired
    ShowService showService;
    
    @GetMapping("/preferred")
    @PreAuthorize("hasRole('REVIEWER')")
    public Page<Show> getLatestPrefrredShows(@RequestBody PaginationPayload paginationPayload, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Page<Show> show = showService.getLatestPrefrredShows(userDetails.getEmail());
        return show;
    }

    @GetMapping("/mostReviewed")
    public List<ShowResponsePayload> getTheMostReviewedShows(@RequestParam int pageSize, @RequestParam int pageNumber ) {
        PaginationPayload paginationPayload = new PaginationPayload();
        paginationPayload.setPageSize(pageSize);
        paginationPayload.setPageNumber(pageNumber);
        List<ShowResponsePayload> mostReviewedShows = showService.getTheMostReviewedShows(paginationPayload);  
        return mostReviewedShows;
    }

    @GetMapping("/wishlist")
    public Page<Wishlist> getShowsAddedToWishlist(@RequestBody PaginationPayload paginationPayload, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Page<Wishlist> wishlistShows = showService.getShowsAddedToWishlist(userDetails.getEmail());
        return wishlistShows;
    }
}
