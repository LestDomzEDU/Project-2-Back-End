package com.example.sportsbook;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.EventController;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock JdbcTemplate jdbcTemplate;
    @InjectMocks EventController controller;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listEvents_empty_returnsEmptyArray() throws Exception {
        when(jdbcTemplate.queryForList(anyString())).thenReturn(List.of());
        mockMvc.perform(get("/api/events").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getEventOdds_empty_returnsEmptyArray() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/api/events/7/odds").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }
}
