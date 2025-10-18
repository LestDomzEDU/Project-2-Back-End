package com.example.sportsbook;

import com.example.sportsbook.bets.*;
import com.example.sportsbook.bets.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BetService betService;

    @InjectMocks
    private BetController betController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(betController).build();
    }

    @Test
    void listAll_returnsEmptyArray() throws Exception {
        when(betService.listAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/bets"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void byUser_returnsBets() throws Exception {
        when(betService.listByUser(7L)).thenReturn(List.of(new Bet()));
        mockMvc.perform(get("/api/bets/user/7"))
                .andExpect(status().isOk());
    }

    @Test
    void byEvent_returnsBets() throws Exception {
        when(betService.listByEvent(3L)).thenReturn(List.of(new Bet()));
        mockMvc.perform(get("/api/bets/event/3"))
                .andExpect(status().isOk());
    }

    @Test
    void create_returns201() throws Exception {
        BetCreateRequest r = new BetCreateRequest();
        when(betService.create(r)).thenReturn(new Bet());
        mockMvc.perform(post("/api/bets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isCreated());
    }

    @Test
    void update_returns200() throws Exception {
        when(betService.update(1L, new BetUpdateRequest())).thenReturn(new Bet());
        mockMvc.perform(put("/api/bets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/bets/1"))
            .andExpect(status().isNoContent());
    }
}