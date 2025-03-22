package com.group30.daily_reading_track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.group30.daily_reading_track.dto.ForgotPasswordRequest;
import com.group30.daily_reading_track.dto.LoginRequest;
import com.group30.daily_reading_track.dto.RegisterRequest;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    // 显示登录页面
    @GetMapping("/loginPage")
    public String showLoginPage() {
        return "login";
    }

    // 显示注册页面
    @GetMapping("/registerPage")
    public String showRegisterPage() {
        return "register";
    }

    // 登录接口
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            model.addAttribute("error", "Error Username OR Password");
            return "login";
        }
        // 判断是否管理员
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "adminPage";
        }
        return "userPage";
    }

    // 登录接口，用于 Postman 测试
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> apiLogin(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                   .body("Error Username OR Password");
        }
        // 判断是否管理员
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.ok("admin");
        }
        return ResponseEntity.ok("user");
    }

    // 注册接口
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest, Model model) {
        boolean result = userService.register(registerRequest);
        if (!result) {
            model.addAttribute("error", "Failed Registration");
            return "register";
        }
        model.addAttribute("message", "Success Registration, Please check the Email");
        return "login";
    }

    // 注册接口，用于 Postman 测试
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<?> apiRegister(@RequestBody RegisterRequest registerRequest) {
        boolean result = userService.register(registerRequest);
        if (!result) {
            return ResponseEntity.badRequest().body("Failed Registration");
        }
        return ResponseEntity.ok("Success Registration, Please check the Email");
    }

    // 找回密码接口
    @PostMapping("/forgotPassword")
    public String forgotPassword(@ModelAttribute ForgotPasswordRequest request, Model model) {
        boolean result = userService.forgotPassword(request.getEmail());
        if (!result) {
            model.addAttribute("error", "Failed to Reset");
            return "forgotPassword";
        }
        model.addAttribute("message", "Success! Please check the Email.");
        return "login";
    }

    // 找回密码接口，用于 Postman 测试
    @PostMapping("/api/forgotPassword")
    @ResponseBody
    public ResponseEntity<?> apiForgotPassword(@RequestBody ForgotPasswordRequest request) {
        boolean result = userService.forgotPassword(request.getEmail());
        if (!result) {
            return ResponseEntity.badRequest().body("Failed to Reset");
        }
        return ResponseEntity.ok("Success! Please check the Email.");
    }
}

