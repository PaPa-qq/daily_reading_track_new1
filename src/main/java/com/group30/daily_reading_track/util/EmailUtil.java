package com.group30.daily_reading_track.util;

import com.group30.daily_reading_track.model.User;

// 模拟邮件发送
public class EmailUtil {
    public static void sendVerificationEmail(User user){
        System.out.println("Send Email to " + user.getEmail());
    }

    // 找回密码
    public static void sendResetPasswordEmail(User user){
        System.out.println("Send Reset Email to " + user.getEmail());
    }
}
