package com.example.Cinematics.service;

import com.example.Cinematics.converter.MovieConverter;
import com.example.Cinematics.dao.*;
import com.example.Cinematics.dto.MovieDTO;
import com.example.Cinematics.dto.MovieLineDTO;
import com.example.Cinematics.dto.MovieSceneDTO;
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MovieLineRepository movieLineRepository;

    @Autowired
    private MovieSceneRepository movieSceneRepository;

    @Autowired
    private OutstandingCreationRepository outstandingCreationRepository;



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



    @Override
    public List<Movie> searchMovies(String name, Integer time, String nation) {
        return movieRepository.searchMovies(name, time, nation);
    }

    @Override
    public MovieDTO getMovieDetail(Long id) {
        // 1. 查电影基础信息
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("电影不存在，ID：" + id));

        // 2. 查关联数据
        List<Comment> comments = commentRepository.findByMovieIdOrderByCreatedAtAsc(id);
        List<MovieLine> lines = movieLineRepository.findByMovieId(id);
        List<MovieScene> scenes = movieSceneRepository.findByMovieId(id);
        List<OutstandingCreation> links = outstandingCreationRepository.findByMovieId(id);

        // 3. 基础信息转 DTO
        MovieDTO dto = MovieConverter.convertMovie(movie);

        // 4. 台词转换（实体 → DTO）
        List<MovieLineDTO> lineDTOs = new java.util.ArrayList<>();
        for (MovieLine line : lines) {
            MovieLineDTO lineDTO = new MovieLineDTO();
            lineDTO.setId(line.getId());
            lineDTO.setQuoteZh(line.getQuoteZh());
            lineDTO.setQuoteOri(line.getQuoteOri());
            lineDTO.setQuoteOrder(line.getQuoteOrder());
            lineDTOs.add(lineDTO);
        }
        dto.setLines(lineDTOs);

        // 5. 画面转换（实体 → DTO）
        List<MovieSceneDTO> sceneDTOs = new java.util.ArrayList<>();
        for (MovieScene scene : scenes) {
            MovieSceneDTO sceneDTO = new MovieSceneDTO();
            sceneDTO.setId(scene.getId());
            sceneDTO.setImageUrl(scene.getImageUrl());
            sceneDTO.setCaption(scene.getCaption());
            sceneDTO.setSceneOrder(scene.getSceneOrder());
            sceneDTOs.add(sceneDTO);
        }
        dto.setScenes(sceneDTOs);

        // 6. 评论和链接直接塞（类型一致，不需要转）
        dto.setComments(comments);
        dto.setLinks(links);

        return dto;
    }

}
