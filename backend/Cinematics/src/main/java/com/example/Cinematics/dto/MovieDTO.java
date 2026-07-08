package com.example.Cinematics.dto;

//展示返回给前端的数据传输对象

import lombok.Data;

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
}
