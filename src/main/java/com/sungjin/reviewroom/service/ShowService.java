package com.sungjin.reviewroom.service;


import java.util.List;

import com.sungjin.reviewroom.dto.PaginationPayload;
import com.sungjin.reviewroom.dto.ShowResponsePayload;
import com.sungjin.reviewroom.entity.Show;
import com.sungjin.reviewroom.entity.Wishlist;

public interface ShowService {
    public List<Show> getAllShows();
    public List<Show> getLatestPrefrredShows(String email, PaginationPayload paginationPayload);
    public List<ShowResponsePayload> getTheMostReviewedShows(PaginationPayload paginationPayload);
    public List<Wishlist> getShowsAddedToWishlist(String email, PaginationPayload paginationPayload);
}
