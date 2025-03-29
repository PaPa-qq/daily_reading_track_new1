package com.group30.daily_reading_track.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group30.daily_reading_track.model.ReadingActivity;
import com.group30.daily_reading_track.model.User;

public interface ReadingActivityRepo extends JpaRepository<ReadingActivity, Long>{
    List<ReadingActivity> findByUser(User user);

    // 根据标题模糊搜索
    List<ReadingActivity> findByUserAndTitleContaining(User user, String title);
    
}
