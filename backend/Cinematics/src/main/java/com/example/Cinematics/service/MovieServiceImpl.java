package com.example.Cinematics.service;

import com.example.Cinematics.converter.MovieConverter;
import com.example.Cinematics.dao.MovieRepository;
import com.example.Cinematics.dao.Movie;
import com.example.Cinematics.dto.MovieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

//服务接口：怎么做
//Ctrl + I 就是让 IDEA 自动生成接口中需要实现的方法的代码模板。

@Service
public class MovieServiceImpl implements MovieService {

    //依赖注入
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(RuntimeException::new);
        return MovieConverter.convertMovie(movie);
    }

    @Override
    public Long addNewMovie(MovieDTO movieDTO) {
        List<Movie> movieList = movieRepository.findByName(movieDTO.getName());
        if(!CollectionUtils.isEmpty(movieList)){  //判断新加入的电影名字是否已经存在
            throw new IllegalStateException("name:" + movieDTO.getName() + " already exist");   //不是空的，说明已经存在，返回错误信息
        }
        Movie movie = movieRepository.save(MovieConverter.convertMovie(movieDTO));
        return movie.getId();
    }

    @Override
    public void deleteMovieById(Long id) {
        movieRepository.findById(id).orElseThrow(() -> new IllegalStateException("id:" + id + " does not exist"));
        movieRepository.deleteById(id);
    }

    @Override
    public MovieDTO updateMovieById(Long id, MovieDTO movieDTO) {
        // 1. 先查原数据
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("id:" + id + "不存在"));

        // 2. 只更新前端传了非空值的字段
        if (movieDTO.getName() != null) {
            movie.setName(movieDTO.getName());
        }
        if (movieDTO.getTime() != null) {
            movie.setTime(movieDTO.getTime());
        }
        if (movieDTO.getType() != null) {
            movie.setType(movieDTO.getType());
        }
        if (movieDTO.getDirector() != null) {
            movie.setDirector(movieDTO.getDirector());
        }
        if (movieDTO.getActor() != null) {
            movie.setActor(movieDTO.getActor());
        }
        if (movieDTO.getNation() != null) {
            movie.setNation(movieDTO.getNation());
        }
        if (movieDTO.getMedium() != null) {
            movie.setMedium(movieDTO.getMedium());
        }

        // 3. 保存
        Movie updatedMovie = movieRepository.save(movie);
        return MovieConverter.convertMovie(updatedMovie);
    }

}
