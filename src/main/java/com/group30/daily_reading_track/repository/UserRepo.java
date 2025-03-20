package com.group30.daily_reading_track.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group30.daily_reading_track.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    boolean existsByEmail(String Email); //检查邮箱是否存在
    boolean existsByUsername(String Username);  //检查用户名是否存在
    // 根据用户名或者邮箱查找用户（登录时）
    Optional<User> findByUsernameOrEmail(String Username, String Email);
}
