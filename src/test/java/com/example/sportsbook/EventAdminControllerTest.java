package com.example.sportsbook;

import com.example.sportsbook.controller.ApiErrorHandler;
import com.example.sportsbook.controller.EventAdminController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EventAdminControllerTest {

    private MockMvc mvc;

    @Mock JdbcTemplate jdbc;
    @InjectMocks EventAdminController controller;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    void updateResult_returns200() throws Exception {
        // Controller executes: update(sql, result, id) → exactly THREE args
        when(jdbc.update(anyString(), eq("HOME"), eq(10L))).thenReturn(1);

        mvc.perform(put("/api/admin/events/10/result")
                .param("result", "HOME"))
           .andExpect(status().isOk());
    }
}
