package com.sungjin.reviewroom.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;

public interface ShowService {
    public Page<Show> getLatestPrefrredShows(String email);
    public List<ShowResponsePayload> getTheMostReviewedShows(PaginationPayload paginationPayload);
    public Page<Wishlist> getShowsAddedToWishlist(String email);
}
