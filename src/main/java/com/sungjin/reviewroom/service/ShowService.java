package com.sungjin.reviewroom.service;


import org.springframework.data.domain.Page;

import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;

public interface ShowService {
    
    public Page<Show> getTheMostReviewedShows(int pageNumber, int pageSize);
    public Page<Show> getLatestPrefrredShows(String email);
    public Page<Wishlist> getShowsAddedToWishlist(String email);

}
