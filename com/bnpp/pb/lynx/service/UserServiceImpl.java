package com.bnpp.pb.lynx.service;

import com.bnpp.pb.lynx.model.LoginResponse;
import com.bnpp.pb.lynx.model.User;
import com.bnpp.pb.lynx.model.UserDTO;
import com.bnpp.pb.lynx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        // Check if username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User(userDTO.getUsername(), userDTO.getPassword());
        return userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(UserDTO userDTO) {
        // Find user by username
        Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            // Check if password matches
            if (user.getPassword().equals(userDTO.getPassword())) {
                return new LoginResponse("success", "Login successful", user);
            } else {
                return new LoginResponse("error", "Invalid password", null);
            }
        } else {
            return new LoginResponse("error", "User not found", null);
        }
    }
}