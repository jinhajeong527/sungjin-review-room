package com.sungjin.reviewroom.dto;


import com.sungjin.reviewroom.entity.Show;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShowResponsePayload {
    Show show;
    long reviewsCount;

    public ShowResponsePayload(Show show, long reviewsCount) {
        this.show = show;
        this.reviewsCount = reviewsCount;
    }

}
