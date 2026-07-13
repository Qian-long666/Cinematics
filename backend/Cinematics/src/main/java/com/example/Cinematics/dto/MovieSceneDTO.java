package com.example.Cinematics.dto;

import lombok.Data;

@Data
public class MovieSceneDTO {
    private Long id;
    private String imageUrl;
    private String caption;
    private Integer sceneOrder;
}