package com.example.sportsbook.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    private final JdbcTemplate jdbc;

    public UserController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/users")
    public List<Map<String,Object>> listUsers() {
        return jdbc.queryForList("""
            SELECT id, email, display_name, created_at
            FROM users
            ORDER BY id ASC
        """);
    }
}
/// implemente a new GET/users endpoint
/// This endpoint queries the users table in the database and returns a list 
/// of all users in JSON format
/// It includes id, email, display_name, and created_at fields.
/// Added @CrossOrigin(origins="*") to allow frontend requests from any origin.
/// This completes one of the MVP REST API endpoints, so now the backend exposes:
///GET /events
/// GET /users
///GET /users/{id}/bets
/// POST /bets