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

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.ApiErrorHandler;
import com.example.sportsbook.controller.BetController;
import com.example.sportsbook.controller.BetRequest;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    @Mock JdbcTemplate jdbcTemplate;
    @InjectMocks BetController controller;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    void getUserBets_empty_returnsEmptyArray() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/api/users/1/bets").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void placeBet_invalidSelection_throwsBadRequest() throws Exception {
        BetRequest req = new BetRequest(1, 1, "DRAW", 1000);
        mockMvc.perform(post("/api/bets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void placeBet_missingEvent_throwsBadRequest() throws Exception {
        BetRequest req = new BetRequest(1, 999, "HOME", 1000);

        when(jdbcTemplate.queryForMap(anyString(), anyLong()))
          .thenThrow(new EmptyResultDataAccessException(1));

        mockMvc.perform(post("/api/bets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isBadRequest());
    }
}
