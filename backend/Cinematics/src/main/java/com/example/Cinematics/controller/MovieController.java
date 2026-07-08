package com.example.Cinematics.controller;

import com.example.Cinematics.Response;
import com.example.Cinematics.dao.Movie;
import com.example.Cinematics.dto.MovieDTO;
import com.example.Cinematics.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


//后端接收前端Http请求并返回数据
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movie/{id}")  //通过 @GetMapping 将 HTTP GET 请求映射到 /movie/{id} 路径
    public Response<MovieDTO> getMovieById(@PathVariable Long id){ //并使用 @PathVariable 将路径中的 {id} 绑定为方法参数，实现按 ID 查询电影资源的接口
        return Response.newSuccess(movieService.getMovieById(id));
    }

    //添加电影信息
    @PostMapping("/movie")
    public Response<Long> addNewMovie(@RequestBody MovieDTO movieDTO){
        return Response.newSuccess(movieService.addNewMovie(movieDTO));
    }


    //根据id删除电影
    @DeleteMapping("/movie/{id}")
    public void deleteMovieById(@PathVariable Long id){
        movieService.deleteMovieById(id);
    }


    //更新电影
    @PutMapping("/movie/{id}")
    public Response<MovieDTO> updateMovieById(
            @PathVariable Long id,
            @RequestBody MovieDTO movieDTO) {

        // 调用 Service 更新
        MovieDTO updated = movieService.updateMovieById(id, movieDTO);
        return Response.newSuccess(updated);
    }
}
