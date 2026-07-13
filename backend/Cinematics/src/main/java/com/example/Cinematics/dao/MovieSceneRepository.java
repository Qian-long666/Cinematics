package com.example.Cinematics.dao;

import com.example.Cinematics.dao.MovieScene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieSceneRepository extends JpaRepository<MovieScene, Long> {

    @Query("SELECT ms FROM MovieScene ms WHERE ms.movieId = :movieId ORDER BY ms.sceneOrder ASC")
    List<MovieScene> findByMovieId(@Param("movieId") Long movieId);
}