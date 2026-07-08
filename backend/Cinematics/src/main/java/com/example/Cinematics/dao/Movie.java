package com.example.Cinematics.dao;


import jakarta.persistence.*;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "cinematics")
public class Movie {

    @Id
    @Column
    @GeneratedValue(strategy = IDENTITY)  //自增的id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "time")
    private Integer time;

    @Column(name = "type")
    private String type;

    @Column(name = "director")
    private String director;

    @Column(name = "actor")
    private String actor;

    @Column(name = "nation")
    private String nation;

    @Column(name = "medium")
    private String medium;

}
