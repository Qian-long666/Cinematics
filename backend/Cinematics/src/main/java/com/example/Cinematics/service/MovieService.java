package com.example.Cinematics.service;

import com.example.Cinematics.dao.Movie;
import com.example.Cinematics.dto.MovieDTO;

//服务实现：能做什么
public interface MovieService {

    //根据id查询
    MovieDTO getMovieById(Long id);

    //添加电影信息
    Long addNewMovie(MovieDTO movieDTO);

    //根据id删除电影
    void deleteMovieById(Long id);

    //修改电影信息
    MovieDTO updateMovieById(Long id, MovieDTO movieDTO);
}
