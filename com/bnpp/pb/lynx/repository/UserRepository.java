package com.bnpp.pb.lynx.repository;

import com.bnpp.pb.lynx.model.User;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    User save(User user);
} 