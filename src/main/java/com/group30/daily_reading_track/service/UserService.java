package com.group30.daily_reading_track.service;


import org.springframework.beans.factory.annotation.Autowired;
import com.group30.daily_reading_track.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.repository.UserRepo;


@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private EmailService emailService;

    // 用户登录：验证用户名和密码
    public User login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 实际项目中应使用加密的密码比对
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    // 用户注册
    public boolean register(RegisterRequest request) {
        try {
            // 检查密码是否一致
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                System.out.println("密码不一致");
                return false;
            }
            // 检查用户名和邮箱是否存在
            Optional<User> userByUsername = userRepository.findByUsername(request.getUsername());
            Optional<User> userByEmail = userRepository.findByEmail(request.getEmail());
            if (userByUsername.isPresent() || userByEmail.isPresent()) {
                System.out.println("用户名或邮箱已存在");
                return false;
            }

            if (!emailService.verifyVerificationCode(request.getEmail(), request.getVerificationCode())){
                System.out.println("验证码错误");
                return false;
            }
            
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setRole("USER");  // 默认注册为普通用户
            user.setEmailVerified(false);
            
            // 保存用户到数据库
            userRepository.save(user);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 判断邮箱是否存在
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // 重置密码（找回密码流程）
    public boolean resetPassword(String email, String verificationCode, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            if (emailService.verifyVerificationCode(email, verificationCode)) {
                User user = optionalUser.get();
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // 找回密码：原有逻辑，此处可保留或移除
    public boolean forgotPassword(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                // 此处可以整合为发送验证码逻辑
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String sendVerificationCode(String email){
        return emailService.sendVerificationCodeEmail(email);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElse(null);
    }
}