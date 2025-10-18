package com.example.sportsbook;

import com.example.sportsbook.controller.EventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    void listEvents_ok() throws Exception {
        when(jdbc.queryForList(anyString())).thenReturn(List.of());
        mockMvc.perform(get("/api/events"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    void getEvent_ok() throws Exception {
        when(jdbc.queryForMap(anyString(), any())).thenReturn(Map.of("id", 1));
        mockMvc.perform(get("/api/events/1"))
            .andExpect(status().isOk());
    }

    @Test
    void getOdds_ok() throws Exception {
        when(jdbc.queryForList(anyString(), any())).thenReturn(List.of());
        when(jdbc.queryForObject(anyString(), eq(Long.class), any())).thenReturn(1L); // existence check
        mockMvc.perform(get("/api/events/1/odds"))
            .andExpect(status().isOk());
    }
}