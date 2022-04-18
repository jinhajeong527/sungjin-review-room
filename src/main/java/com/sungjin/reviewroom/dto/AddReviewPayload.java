package com.sungjin.reviewroom.dto;

import java.util.List;

import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.entity.Review;

import com.sungjin.reviewroom.entity.Show;

import lombok.Data;

@Data
public class AddReviewPayload {
    private Review review;
    private Show show;
    private List<Genre> genres;
}
