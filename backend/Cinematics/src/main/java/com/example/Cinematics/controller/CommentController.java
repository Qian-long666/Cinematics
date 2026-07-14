package com.example.Cinematics.controller;

import com.example.Cinematics.Response;
import com.example.Cinematics.dto.CommentDTO;
import com.example.Cinematics.dto.CommentRequest;
import com.example.Cinematics.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发表评论
     */
    @PostMapping
    public Response<?> addComment(@RequestBody CommentRequest request) {
        try {
            String username = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            CommentDTO dto = commentService.addComment(
                    request.getMovieId(), request.getContent(), username);
            return Response.newSuccess(dto);
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }

    /**
     * 获取某电影的所有评论
     */
    @GetMapping("/movie/{movieId}")
    public Response<?> getCommentsByMovie(@PathVariable Long movieId) {
        try {
            return Response.newSuccess(commentService.getCommentsByMovie(movieId));
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }

    /**
     * 删除评论（仅本人或管理员）
     */
    @DeleteMapping("/{id}")
    public Response<?> deleteComment(@PathVariable Long id) {
        try {
            String username = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            commentService.deleteComment(id, username);
            return Response.newSuccess(null);
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }

    /**
     * 获取当前用户的所有评论（时间轴）
     */
    @GetMapping("/my")
    public Response<?> getMyComments() {
        try {
            String username = SecurityContextHolder.getContext()
                    .getAuthentication().getName();
            return Response.newSuccess(commentService.getMyComments(username));
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }
}
