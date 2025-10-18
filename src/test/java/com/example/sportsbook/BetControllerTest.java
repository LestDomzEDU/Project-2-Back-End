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

@ExtendWith(MockitoExtension.class)
class BetControllerTest {

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
    }
}
