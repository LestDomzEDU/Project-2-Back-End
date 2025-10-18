package com.example.sportsbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.ApiErrorHandler;
import com.example.sportsbook.controller.EventAdminController;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class EventAdminControllerTest {

    @Mock JdbcTemplate jdbcTemplate;
    @InjectMocks EventAdminController controller;

    MockMvc mockMvc;
    ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    void createEvent_validRequest_returnsEventId() throws Exception {
        // We don’t rely on generated key since controller falls back to 1 if missing
        mockMvc.perform(post("/admin/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(Map.of(
                        "home", "Lakers",
                        "away", "Celtics",
                        "startsAt", "2025-10-17T00:00:00Z"
                ))))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.eventId").isNumber());
    }

    @Test
    void setResult_invalidResult_returns400() throws Exception {
        mockMvc.perform(put("/admin/events/1/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(Map.of("result", "DRAW"))))
            .andExpect(status().isBadRequest());
    }

    @Test
    void setResult_validResult_updatesBets() throws Exception {
        when(jdbcTemplate.update(anyString(), any(), anyLong())).thenReturn(1);
        when(jdbcTemplate.update(anyString(), anyLong(), any())).thenReturn(1);

        mockMvc.perform(put("/admin/events/10/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(Map.of("result", "HOME"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.eventId").value(10))
            .andExpect(jsonPath("$.result").value("HOME"))
            .andExpect(jsonPath("$.betsWon").isNumber())
            .andExpect(jsonPath("$.betsLost").isNumber());

        verify(jdbcTemplate).update("UPDATE events SET status='FINISHED', result=? WHERE id=?", "HOME", 10L);
    }
}
