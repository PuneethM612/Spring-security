package com.bnpp.pb.lynx.service;

import com.bnpp.pb.lynx.model.LoginResponse;
import com.bnpp.pb.lynx.model.User;
import com.bnpp.pb.lynx.model.UserDTO;

public interface UserService {
    
    User registerUser(UserDTO userDTO);
    
    LoginResponse loginUser(UserDTO userDTO);
} 