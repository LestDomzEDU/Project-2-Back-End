package com.example.sportsbook;

<<<<<<< HEAD
import java.util.List;
import java.util.Map;

=======
import com.example.sportsbook.controller.UserController;
>>>>>>> origin/main
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
<<<<<<< HEAD
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
=======
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
>>>>>>> origin/main

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

<<<<<<< HEAD
    private MockMvc mockMvc;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void listUsers_returnsAllUsers() throws Exception {
        when(jdbcTemplate.queryForList("""
            SELECT id, email, display_name, created_at
            FROM users
            ORDER BY id ASC
        """)).thenReturn(List.of(
            Map.of("id", 1L, "email", "alice@example.com", "display_name", "Alice", "created_at", "2025-10-01T10:00:00Z"),
            Map.of("id", 2L, "email", "bob@example.com", "display_name", "Bob", "created_at", "2025-10-02T11:00:00Z")
        ));

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].email").value("alice@example.com"))
            .andExpect(jsonPath("$[1].display_name").value("Bob"));
    }

    @Test
    void listUsers_emptyList_returnsEmptyJsonArray() throws Exception {
        when(jdbcTemplate.queryForList("""
            SELECT id, email, display_name, created_at
            FROM users
            ORDER BY id ASC
        """)).thenReturn(List.of());

        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }
}
=======
    private MockMvc mvc;

    @Mock JdbcTemplate jdbc;

    @InjectMocks UserController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listUsers_ok() throws Exception {
        when(jdbc.queryForList(anyString())).thenReturn(new ArrayList<>());
        mvc.perform(get("/api/users"))
           .andExpect(status().isOk())
           .andExpect(content().json("[]"));
    }
}
>>>>>>> origin/main
