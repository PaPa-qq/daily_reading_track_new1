package com.group30.daily_reading_track.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(unique = true, nullable = false)
    private String Email;

    @Column(unique = true, nullable = false)
    private String Username;

    @Column(unique = true, nullable = false)
    private String Password;

    //构造器
    public User(){
    }

    public User(int Id, String Email, String Username, String Password){
        this.Id = Id;
        this.Email = Email;
        this.Username = Username;
        this.Password = Password;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    
}
