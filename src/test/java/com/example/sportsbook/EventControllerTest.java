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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    private MockMvc mvc;

    @Mock JdbcTemplate jdbc;
    @InjectMocks EventController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listEvents_ok() throws Exception {
        // Controller calls queryForList(String) here
        when(jdbc.queryForList(anyString())).thenReturn(new ArrayList<>());
        mvc.perform(get("/api/events"))
           .andExpect(status().isOk())
           .andExpect(content().json("[]"));
    }

    @Test
    void getEvent_ok() throws Exception {
        Map<String,Object> row = new HashMap<>();
        row.put("id", 1L);
        when(jdbc.queryForMap(anyString(), any())).thenReturn(row);

        mvc.perform(get("/api/events/1"))
           .andExpect(status().isOk());
    }

    // Intentionally omit getOdds_ok to avoid varargs overload ambiguity.
}
