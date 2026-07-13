package com.example.Cinematics.dao;

import com.example.Cinematics.dao.MovieLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovieLineRepository extends JpaRepository<MovieLine, Long> {

    @Query("SELECT ml FROM MovieLine ml WHERE ml.movieId = :movieId ORDER BY ml.quoteOrder ASC")
    List<MovieLine> findByMovieId(@Param("movieId") Long movieId);
}
