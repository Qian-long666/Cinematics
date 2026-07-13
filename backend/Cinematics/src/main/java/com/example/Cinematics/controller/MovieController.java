package com.example.Cinematics.controller;

import com.example.Cinematics.Response;
import com.example.Cinematics.dao.Movie;
import com.example.Cinematics.dto.MovieDTO;
import com.example.Cinematics.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // ===== 电影详情（含台词、画面、评论、链接） =====
    @GetMapping("/movie/{id}")
    public Response<MovieDTO> getMovieDetail(@PathVariable Long id) {
        return Response.newSuccess(movieService.getMovieDetail(id));
    }

    // ===== 添加电影 =====
    @PostMapping("/movie")
    public Response<Long> addNewMovie(@RequestBody MovieDTO movieDTO) {
        return Response.newSuccess(movieService.addNewMovie(movieDTO));
    }

    // ===== 删除电影 =====
    @DeleteMapping("/movie/{id}")
    public void deleteMovieById(@PathVariable Long id) {
        movieService.deleteMovieById(id);
    }

    // ===== 更新电影 =====
    @PutMapping("/movie/{id}")
    public Response<MovieDTO> updateMovieById(
            @PathVariable Long id,
            @RequestBody MovieDTO movieDTO) {
        MovieDTO updated = movieService.updateMovieById(id, movieDTO);
        return Response.newSuccess(updated);
    }

    // ===== 用户多条件查询 =====
    @GetMapping("/movies/search")
    public Response<List<Movie>> searchMovies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer time,
            @RequestParam(required = false) String nation) {
        List<Movie> movies = movieService.searchMovies(name, time, nation);
        return Response.newSuccess(movies);
    }
}