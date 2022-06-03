package com.sungjin.reviewroom.service;

import java.util.List;

import javax.transaction.Transactional;

import com.sungjin.reviewroom.dao.GenreRepository;
import com.sungjin.reviewroom.entity.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Override
    @Transactional
    public List<Genre> getGenres() {
        System.out.println(genreRepository.findAll());
        return genreRepository.findAll();
    }
}
