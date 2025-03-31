package com.group30.daily_reading_track.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 这个是用来加载图片的
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射：浏览器访问 /avatars/** 实际读取本地 uploads/avatars 文件夹
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + System.getProperty("user.dir") + "/uploads/avatars/");
    }
}
