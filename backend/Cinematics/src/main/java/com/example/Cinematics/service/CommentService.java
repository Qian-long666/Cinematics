package com.example.Cinematics.service;

import com.example.Cinematics.dto.CommentDTO;

import java.util.List;

public interface CommentService {

    /** 发表评论 */
    CommentDTO addComment(Long movieId, String content, String username);

    /** 获取某电影的所有评论（按时间正序） */
    List<CommentDTO> getCommentsByMovie(Long movieId);

    /** 获取某用户的所有评论（按时间正序） */
    List<CommentDTO> getMyComments(String username);

    /** 删除评论（仅本人或管理员可删） */
    void deleteComment(Long commentId, String username);
}
