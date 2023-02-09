package com.sungjin.reviewroom.controller;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;
import com.sungjin.reviewroom.security.UserDetailsImpl;
import com.sungjin.reviewroom.service.ShowService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "${spring.data.rest.base-path}/show")
public class ShowController {
    
    @Autowired
    ShowService showService;
    @PostMapping("/all")
    public List<Show> getAllShows(@RequestBody Map<String, Integer> map) {
        System.out.println(map.get("pageNumber"));
        System.out.println(map.get("pageSize"));
        List<Show> shows = showService.getAllShows();
        return shows;
    }

    @GetMapping("/preferred")
    public List<Show> getLatestPrefrredShows(@RequestParam int pageNumber, @RequestParam int pageSize, Authentication authentication) {
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<Show> shows = showService.getLatestPrefrredShows(userDetails.getEmail(), paginationPayload);
        return shows;
    }

    @GetMapping("/mostReviewed")
    public List<ShowResponsePayload> getTheMostReviewedShows(@RequestParam int pageNumber, @RequestParam int pageSize) {
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        List<ShowResponsePayload> mostReviewedShows = showService.getTheMostReviewedShows(paginationPayload);  
        return mostReviewedShows;
    }
    // TODO: 스프링 시큐리티를 Authentication authentication 이렇게 인자로 넣어서 사용하는게 맞는지는 학습이 필요하다.
    @GetMapping("/wishlist")
    public List<Wishlist> getShowsAddedToWishlist(@RequestParam int pageNumber, @RequestParam int pageSize, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        PaginationPayload paginationPayload = new PaginationPayload(pageNumber, pageSize);
        List<Wishlist> wishlistShows = showService.getShowsAddedToWishlist(userDetails.getEmail(), paginationPayload);
        return wishlistShows;
    }
}
