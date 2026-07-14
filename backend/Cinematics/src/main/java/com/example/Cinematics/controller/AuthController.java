package com.example.Cinematics.controller;

import com.example.Cinematics.Response;
import com.example.Cinematics.dao.User;
import com.example.Cinematics.dao.UserRepository;
import com.example.Cinematics.dto.AuthResponse;
import com.example.Cinematics.dto.LoginRequest;
import com.example.Cinematics.dto.RegisterRequest;
import com.example.Cinematics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * 注册（默认创建 USER 角色账户）
     */
    @PostMapping("/register")
    public Response<?> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse auth = userService.register(request.getUsername(), request.getPassword());
            return Response.newSuccess(auth);
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Response<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse auth = userService.login(request.getUsername(), request.getPassword());
            return Response.newSuccess(auth);
        } catch (RuntimeException e) {
            return Response.newFail(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public Response<?> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return Response.newFail("用户不存在");
        }
        return Response.newSuccess(new AuthResponse(null, user.getUsername(), user.getRole()));
    }
}
