package com.sungjin.reviewroom.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.sungjin.reviewroom.entity.Show;

public interface ShowService {
    public Set<Show> getLatestPrefrredShows(String email);
    public Page<Show> getTheMostReviewedShows();
    public Set<Show> getShowsAddedToWishlist(String email);
}