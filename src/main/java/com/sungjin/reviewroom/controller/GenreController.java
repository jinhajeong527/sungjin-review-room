package com.sungjin.reviewroom.controller;

import java.util.List;

import com.sungjin.reviewroom.entity.Genre;
import com.sungjin.reviewroom.service.GenreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/genre")
public class GenreController {
    
    @Autowired
    GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        List<Genre> genres = genreService.getGenres();
        return genres;
    }
    
}
