package com.group30.daily_reading_track.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group30.daily_reading_track.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
