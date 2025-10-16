package com.example.sportsbook;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.sportsbook.controller.EventAdminController;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class EventAdminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private EventAdminController adminController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Map IllegalArgumentException -> 400 for standalone MockMvc
    @RestControllerAdvice
    static class TestAdvice {
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(IllegalArgumentException.class)
        void handleIllegalArgument(IllegalArgumentException ex) {}
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(adminController)
            .setControllerAdvice(new TestAdvice())
            .build();
    }

    @Test
    void createEvent_validRequest_returnsEventId() throws Exception {
        // Only stub the INSERT ... RETURNING id (remove unused/extra stubs)
        doReturn(10L).when(jdbcTemplate)
            .queryForObject(startsWith("INSERT"), eq(Long.class), any(Object[].class));

        String json = objectMapper.writeValueAsString(Map.of(
            "league", "NBA",
            "homeTeam", "Lakers",
            "awayTeam", "Celtics",
            "startTime", "2025-10-20T19:00:00Z"
        ));

        mockMvc.perform(post("/admin/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated());
    }

    @Test
    void setResult_invalidResult_throwsException() throws Exception {
        String json = objectMapper.writeValueAsString(Map.of("result", "DRAW"));

        mockMvc.perform(put("/admin/events/1/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isBadRequest()); // handled by TestAdvice
    }

    @Test
    void setResult_validResult_updatesBets() throws Exception {
        // Vararg-aware matcher for update()
        doReturn(1).when(jdbcTemplate)
            .update(eq("UPDATE events SET result = ?, status = 'FINISHED' WHERE id = ?"),
                    any(Object[].class));

        String json = objectMapper.writeValueAsString(Map.of("result", "HOME"));

        mockMvc.perform(put("/admin/events/1/result")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk());
    }
}
