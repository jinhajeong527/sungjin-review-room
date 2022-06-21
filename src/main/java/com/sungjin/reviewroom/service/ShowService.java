package com.sungjin.reviewroom.service;

import java.util.Set;

import com.sungjin.reviewroom.entity.Show;

public interface ShowService {
    public Set<Show> getLatestPrefrredShows(String email);
}
