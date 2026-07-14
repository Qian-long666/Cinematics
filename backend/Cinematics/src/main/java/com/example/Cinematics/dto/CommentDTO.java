package com.example.Cinematics.dto;

import com.example.Cinematics.dao.Comment;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class CommentDTO {
    private Long id;
    private Long movieId;
    private String movieName;
    private String content;
    private String username;
    private String role;        // USER 或 ADMIN
    private String createdAt;   // 格式化后的日期字符串，如 "7月13日"

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("M月d日");

    public static CommentDTO from(Comment c) {
        CommentDTO dto = new CommentDTO();
        dto.setId(c.getId());
        dto.setMovieId(c.getMovieId());
        dto.setContent(c.getContent());
        if (c.getUser() != null) {
            dto.setUsername(c.getUser().getUsername());
            dto.setRole(c.getUser().getRole());
        }
        LocalDateTime t = c.getCreatedAt();
        dto.setCreatedAt(t != null ? t.format(FMT) : "");
        return dto;
    }
}
