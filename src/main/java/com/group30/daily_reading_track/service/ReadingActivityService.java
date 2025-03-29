package com.group30.daily_reading_track.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group30.daily_reading_track.model.ReadingActivity;
import com.group30.daily_reading_track.model.User;
import com.group30.daily_reading_track.repository.ReadingActivityRepo;

@Service
public class ReadingActivityService {
    
    @Autowired
    private ReadingActivityRepo readingActivityRepo;

    // 创建新的阅读活动
    public ReadingActivity createActivity(ReadingActivity readingActivity){
        return readingActivityRepo.save(readingActivity);
    }

    // 更新阅读活动
    public ReadingActivity updateActivity(ReadingActivity readingActivity){
        return readingActivityRepo.save(readingActivity);
    }

    // 删除阅读活动
    public void deleteActivity(Long id){
        readingActivityRepo.deleteById(id);
    }

    // 根据用户获取所有活动
    public List<ReadingActivity> getActivityByUser(User user){
        return readingActivityRepo.findByUser(user);
    }

    // 根据单个id查询
    public ReadingActivity getReadingActivityById(Long id){
        return readingActivityRepo.findById(id).orElse(null);
    }

    // 根据标题模糊搜索
    public List<ReadingActivity> searchActivity(User user, String title){
        return readingActivityRepo.findByUserAndTitleContaining(user, title);
    }
}
