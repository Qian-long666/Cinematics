package com.example.Cinematics.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /** 按电影 ID 查所有评论，按时间正序（旧→新） */
    List<Comment> findByMovieIdOrderByCreatedAtAsc(Long movieId);

    /** 按用户 ID 查所有评论，按时间正序（旧→新） */
    List<Comment> findByUserIdOrderByCreatedAtAsc(Long userId);
}
