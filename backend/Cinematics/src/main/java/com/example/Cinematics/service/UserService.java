package com.example.Cinematics.service;

import com.example.Cinematics.dto.AuthResponse;

public interface UserService {

    /**
     * 注册新用户（默认角色 USER）
     */
    AuthResponse register(String username, String password);

    /**
     * 登录 — 验证用户名密码，返回 JWT
     */
    AuthResponse login(String username, String password);
}
