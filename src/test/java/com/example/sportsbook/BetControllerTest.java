<<<<<<< HEAD
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
=======
package com.example.sportsbook;

import com.example.sportsbook.bets.BetController;
import com.example.sportsbook.bets.BetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
>>>>>>> origin/main

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

<<<<<<< HEAD
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
=======
    private MockMvc mvc;

    @Mock BetService betService;
    @InjectMocks BetController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listAll_ok() throws Exception {
        when(betService.listAll()).thenReturn(List.of());
        mvc.perform(get("/api/bets"))
           .andExpect(status().isOk())
           .andExpect(content().json("[]"));
    }

    @Test
    void byUser_ok() throws Exception {
        when(betService.listByUser(1L)).thenReturn(List.of());
        mvc.perform(get("/api/bets/user/1"))
           .andExpect(status().isOk());
    }

    @Test
    void byEvent_ok() throws Exception {
        when(betService.listByEvent(2L)).thenReturn(List.of());
        mvc.perform(get("/api/bets/event/2"))
           .andExpect(status().isOk());
    }

    @Test
    void delete_noContent_andDelegates() throws Exception {
        mvc.perform(delete("/api/bets/123"))
           .andExpect(status().isNoContent());
        verify(betService).delete(anyLong());
>>>>>>> origin/main
    }
}
