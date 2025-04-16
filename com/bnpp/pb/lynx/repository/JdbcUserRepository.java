package com.bnpp.pb.lynx.repository;

import com.bnpp.pb.lynx.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = jdbcTemplate.queryForObject(
                "SELECT id, username, password FROM users WHERE username = ?",
                userRowMapper,
                username
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = ?",
            Integer.class,
            username
        );
        return count != null && count > 0;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Insert new user
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                return ps;
            }, keyHolder);

            user.setId(keyHolder.getKey().longValue());
        } else {
            // Update existing user
            jdbcTemplate.update(
                "UPDATE users SET username = ?, password = ? WHERE id = ?",
                user.getUsername(),
                user.getPassword(),
                user.getId()
            );
        }
        return user;
    }
} 