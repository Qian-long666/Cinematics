package com.example.Cinematics.dao;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_scenes")
@Data
public class MovieScene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    private String caption;

    @Column(name = "scene_order")
    private Integer sceneOrder = 0;

}