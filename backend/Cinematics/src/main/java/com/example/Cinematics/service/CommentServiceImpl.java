package com.example.Cinematics.service;

import com.example.Cinematics.dao.*;
import com.example.Cinematics.dto.CommentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public CommentServiceImpl(CommentRepository commentRepository,
                              UserRepository userRepository,
                              MovieRepository movieRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    @Transactional
    public CommentDTO addComment(Long movieId, String content, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Comment comment = new Comment();
        comment.setMovieId(movieId);
        comment.setContent(content);
        comment.setUser(user);
        comment.setUsername(user.getUsername());
        comment = commentRepository.save(comment);

        CommentDTO dto = CommentDTO.from(comment);
        movieRepository.findById(movieId)
                .ifPresent(m -> dto.setMovieName(m.getName()));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByMovie(Long movieId) {
        return commentRepository.findByMovieIdOrderByCreatedAtAsc(movieId)
                .stream()
                .map(c -> {
                    CommentDTO dto = CommentDTO.from(c);
                    movieRepository.findById(c.getMovieId())
                            .ifPresent(m -> dto.setMovieName(m.getName()));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getMyComments(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return commentRepository.findByUserIdOrderByCreatedAtAsc(user.getId())
                .stream()
                .map(c -> {
                    CommentDTO dto = CommentDTO.from(c);
                    movieRepository.findById(c.getMovieId())
                            .ifPresent(m -> dto.setMovieName(m.getName()));
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 仅本人或管理员可删
        if (!comment.getUser().getId().equals(user.getId())
                && !"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("无权删除此评论");
        }

        commentRepository.delete(comment);
    }
}
