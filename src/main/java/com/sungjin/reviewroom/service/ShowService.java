package com.sungjin.reviewroom.service;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;

public interface ShowService {
    public Set<Show> getLatestPrefrredShows(String email);
    public Page<Show> getTheMostReviewedShows();
    public Page<Wishlist> getShowsAddedToWishlist(String email);
}
