package com.group30.daily_reading_track.service;


import org.springframework.beans.factory.annotation.Autowired;
import com.group30.daily_reading_track.dto.RegisterRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.repository.UserRepo;
import com.group30.daily_reading_track.util.EmailUtil;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;

    // 用户登录：验证用户名和密码
    public User login(String useername, String password){
        Optional<User> optionalUser = userRepository.findByUsername(useername);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            if (user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    // 用户注册
    public boolean register(RegisterRequest request){
        // 检查两次密码是否输入一致
        if (!request.getPassword().equals(request.getConfirmPassword())){
            return false;
        }

        // 检查用户名和邮箱是否都已存在
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
        userRepository.findByEmail(request.getEmail()).isPresent()){
            return false;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole("USER");
        user.setEmailVerified(false);
        userRepository.save(user);

        // 发送邮箱验证邮件，有EmailUtil完成
        EmailUtil.sendVerificationEmail(user);
        return true;
    }

    public boolean forgotPassword(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            // 生成充值验证码，通过邮件发送
            EmailUtil.sendResetPasswordEmail(user);
            return true;
        }

        return false;
    }
}
