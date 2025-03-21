package com.group30.daily_reading_track.util;

import com.group30.daily_reading_track.model.User;



public class EmailUtil {

    // 模拟发送邮箱验证邮件
    public static void sendVerificationEmail(User user) {
        System.out.println("向 " + user.getEmail() + " 发送验证邮件。");
        // 实际应使用 JavaMailSender 或其他邮件服务实现邮件发送
    }

    // 模拟发送重置密码邮件
    public static void sendResetPasswordEmail(User user) {
        System.out.println("向 " + user.getEmail() + " 发送重置密码邮件。");
        // 实际应生成重置链接或验证码，然后调用邮件服务发送邮件
    }
}
