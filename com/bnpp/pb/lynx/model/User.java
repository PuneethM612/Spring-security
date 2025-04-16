package com.bnpp.pb.lynx.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    private Long id;
    private String username;
    private String password;
    
    // Constructor with username and password
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
} 