package com.example.Cinematics.dao;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "movie_lines")
@Data
public class MovieLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "quote_zh", columnDefinition = "TEXT")
    private String quoteZh;              // 中文台词

    @Column(name = "quote_ori", columnDefinition = "TEXT")
    private String quoteOri;              // 原版台词

    @Column(name = "quote_order")
    private Integer quoteOrder = 0;      // 显示顺序

}