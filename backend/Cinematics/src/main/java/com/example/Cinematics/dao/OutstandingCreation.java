package com.example.Cinematics.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "outstanding_creation")
@Data
public class OutstandingCreation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    private String platform;

    @Column(nullable = false, length = 500)
    private String url;

    private String title;

}