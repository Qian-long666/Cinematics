package com.example.Cinematics.converter;

import com.example.Cinematics.dao.Movie;
import com.example.Cinematics.dto.MovieDTO;


//API 层和数据库层必须解耦
//避免因数据库改动而破坏前端页面
public class MovieConverter {

    public static MovieDTO convertMovie(Movie movie){
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setName(movie.getName());
        movieDTO.setTime(movie.getTime());
        movieDTO.setNation(movie.getNation());
        movieDTO.setType(movie.getType());
        movieDTO.setDirector(movie.getDirector());
        movieDTO.setActor(movie.getActor());
        movieDTO.setMedium(movie.getMedium());
        movieDTO.setPoster_url(movie.getPosterUrl());

        return movieDTO;
    }

    public static Movie convertMovie(MovieDTO movieDTO){
        Movie movie = new Movie();
        movie.setName(movieDTO.getName());
        movie.setTime(movieDTO.getTime());
        movie.setNation(movieDTO.getNation());
        movie.setType(movieDTO.getType());
        movie.setDirector(movieDTO.getDirector());
        movie.setActor(movieDTO.getActor());
        movie.setPosterUrl(movieDTO.getPoster_url());
        movie.setMedium("电影");

        return movie;
    }
}
