package com.group30.daily_reading_track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group30.daily_reading_track.dto.LoginRequest;
import com.group30.daily_reading_track.dto.RegisterRequest;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    // 登录接口
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Error Username OR Password");
        }

        // 如果用户为管理员，则返回特定信息
        if ("ADMIN".equalsIgnoreCase(user.getRole())){
            // 返回一个标志，由前端根据特定标志跳转
            return ResponseEntity.ok("admin");
        }
        return ResponseEntity.ok("user");
    }

    // 注册接口
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        boolean result = userService.register(registerRequest);
        if (!result) {
            return ResponseEntity.badRequest().body("Failed Registration");
        }
        return ResponseEntity.ok("Success Registration, Please check the Email");
    }

    // 找回密码接口
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody String email){
        boolean result = userService.forgotPassword(email);
        if (!result){
            return ResponseEntity.badRequest().body("Failed to Reset");
        }
        return ResponseEntity.ok("Success! Please check the Email.");
    }
}
