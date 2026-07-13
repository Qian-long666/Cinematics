package com.example.Cinematics.dao;

import com.example.Cinematics.dao.OutstandingCreation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OutstandingCreationRepository extends JpaRepository<OutstandingCreation, Long> {

    @Query("SELECT oc FROM OutstandingCreation oc WHERE oc.movieId = :movieId")
    List<OutstandingCreation> findByMovieId(@Param("movieId") Long movieId);
}