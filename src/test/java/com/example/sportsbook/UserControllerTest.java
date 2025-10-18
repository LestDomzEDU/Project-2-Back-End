package com.example.sportsbook;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.UserController;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock JdbcTemplate jdbcTemplate;
    @InjectMocks UserController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listUsers_emptyList_returnsEmptyJsonArray() throws Exception {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of());

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void listUsers_someUsers_returnsArray() throws Exception {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of(
            Map.of("id", 1, "email", "a@x.com", "display_name", "A", "created_at", "2024-01-01T00:00:00Z")
        ));

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].email").value("a@x.com"))
               .andExpect(jsonPath("$[0].display_name").value("A"));
    }
}
