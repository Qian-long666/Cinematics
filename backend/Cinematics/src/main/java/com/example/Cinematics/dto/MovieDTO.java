package com.example.Cinematics.dto;

//展示返回给前端的数据传输对象

import com.example.Cinematics.dao.Comment;
import com.example.Cinematics.dao.OutstandingCreation;
import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {

    private Long id;
    private String name;
    private String nation;
    private Integer time;
    private String type;
    private String director;
    private String actor;
    private String medium;
    private String poster_url;

    // ===== 关联数据 =====
    private List<MovieLineDTO> lines;              // 经典台词
    private List<MovieSceneDTO> scenes;            // 经典画面
    private List<Comment> comments;                // 评论（直接用实体类）
    private List<OutstandingCreation> links;       // 创作链接（直接用实体类）
}
