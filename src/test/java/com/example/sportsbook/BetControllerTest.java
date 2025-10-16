// in this file mocks the databse so you dont need a real DB
//tests GET for a valid user and invalid user
//tests POST for invalid selection (should return 400) 

package com.example.sportsbook;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.sportsbook.controller.BetController;
import com.example.sportsbook.controller.BetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BetController betController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(betController).build();
    }

    @Test
    void listUserBets_validUser_returnsBets() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), anyLong()))
            .thenReturn(List.of(Map.of(
                "id", 1L,
                "event_id", 2L,
                "stake_cents", 1000
            )));

        mockMvc.perform(get("/api/users/1/bets"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].stake_cents").value(1000));
    }

    @Test
    void listUserBets_invalidUser_returnsEmptyList() throws Exception {
        when(jdbcTemplate.queryForList(anyString(), anyLong()))
            .thenReturn(List.of());

        mockMvc.perform(get("/api/users/999/bets"))
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
}
