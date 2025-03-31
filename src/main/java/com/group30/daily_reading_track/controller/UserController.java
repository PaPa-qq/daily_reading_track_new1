package com.group30.daily_reading_track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.group30.daily_reading_track.dto.ForgotPasswordRequest;
import com.group30.daily_reading_track.dto.LoginRequest;
import com.group30.daily_reading_track.dto.RegisterRequest;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.service.EmailService;
import com.group30.daily_reading_track.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

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

    // 新增：显示忘记密码页面（输入邮箱获取验证码）
    @GetMapping("/forgotPasswordPage")
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    // POST接口：发送找回密码验证码
    @PostMapping("/sendForgotPasswordCode")
    @ResponseBody
    public ResponseEntity<?> sendForgotPasswordCode(@RequestParam String email) {
        if (!userService.emailExists(email)) {
            return ResponseEntity.badRequest().body("邮箱不存在");
        }
        String code = emailService.sendForgotPasswordCodeEmail(email);
        if (code == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("验证码发送失败");
        }
        return ResponseEntity.ok("验证码已发送");
    }

    // 处理重置密码请求
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("verificationCode") String verificationCode,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "两次密码输入不一致");
            model.addAttribute("email", email);
            return "resetPassword";
        }
        boolean result = userService.resetPassword(email, verificationCode, newPassword);
        if (result) {
            model.addAttribute("message", "密码重置成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "验证码错误或邮箱不存在");
            model.addAttribute("email", email);
            return "resetPassword";
        }
    }

    // 显示重置密码页面（输入验证码和新密码）
    @GetMapping("/resetPasswordPage")
    public String showResetPasswordPage(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "resetPassword";
    }

    // 登录接口
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, HttpSession session) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            model.addAttribute("error", "Error Username OR Password");
            return "login";
        }
        session.setAttribute("user", user);
        // 根据角色重定向到不同界面
    if ("ADMIN".equalsIgnoreCase(user.getRole())) {
        return "redirect:/admin/dashboard";  // 管理员界面
    }
    return "redirect:/user/dashboard";  // 普通用户仪表盘
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

    // 发送验证码接口
    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public ResponseEntity<?> sendVerificationCode(@RequestParam String email) {
        String code = emailService.sendVerificationCodeEmail(email);
        if (code == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification code");
        }
        return ResponseEntity.ok("Verification code sent successfully");
    }

    // 新增I：用于显示用户管理
    @GetMapping("/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("user", user);
        return "userDashboard";  // 对应 resources/templates/userDashboard.html
    }

    // 新增I：退出登录
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 使 session 失效，清除所有用户数据
        session.invalidate();
        // 重定向到登录页面
        return "redirect:/user/loginPage";
    }

    // 新增I：更新个人资料
    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam String oldUsername,
    @RequestParam String newUsername,
    HttpSession session,
    Model model){
        User currentUser = userService.findByUsername(oldUsername);
        if (currentUser == null){
            model.addAttribute("error", "User not found");
            return "error";
        }

        // 更新逻辑
        currentUser.setUsername(newUsername);
        userService.updateUser(currentUser);
        session.setAttribute("user", currentUser);
        return "redirect:/user/dashboard";
    }

    // 新增I：显示个人资料更新页面
    @GetMapping("/profilePage")
    public String showProfilePage(@RequestParam String username, Model model) {
        User user = userService.findByUsername(username);
        if (user == null){
            model.addAttribute("error", "User not found");
            return "error";
        }
        model.addAttribute("user", user);
        return "updateProfile";
    }

    // 新增I：修改密码页面
    @GetMapping("/changePasswordPage")
    public String showChangePasswordPage(Model model, HttpSession session){
        User currentUser = (User)session.getAttribute("user");
        model.addAttribute("user", currentUser);
        return "changePassword";
    }

    // 新增I：处理密码修改
    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String oldPassword,
    @RequestParam String newPassword,
    @RequestParam String confirmPassword,
    HttpSession session,
    Model model){
    // 从 session 获取当前登录的用户信息
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        // 如果用户未登录，重定向到登录页面
        return "redirect:/user/loginPage";
    }

    // 验证旧密码是否正确
    if (!currentUser.getPassword().equals(oldPassword)) {
        model.addAttribute("error", "旧密码不正确");
            model.addAttribute("username", currentUser.getUsername());
            return "changePassword";
    }

    // 验证新密码和确认密码是否一致
    if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("error", "新密码和确认密码不一致");
        model.addAttribute("username", currentUser.getUsername());
        return "changePassword";
    }

    currentUser.setPassword(newPassword);
    userService.updateUser(currentUser);
    // 更新 session 中的用户信息
    currentUser.setPassword(newPassword); // 注意：实际应用中应对密码进行加密处理
    session.setAttribute("user", currentUser);
    
    model.addAttribute("message", "密码修改成功");
    model.addAttribute("username", currentUser.getUsername());
    return "changePassword";
    }

}

