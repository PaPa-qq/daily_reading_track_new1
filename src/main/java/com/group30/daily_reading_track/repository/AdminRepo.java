package com.group30.daily_reading_track.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group30.daily_reading_track.model.Admin;

@Repository 
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    
}
