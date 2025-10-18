package com.example.sportsbook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sportsbook.controller.ApiErrorHandler;

class ApiErrorHandlerTest {

    @RestController
    static class ThrowingController {
        @GetMapping("/boom-illegal")
        public String boomIllegal() {
            throw new IllegalArgumentException("selection must be HOME or AWAY");
        }
    }

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ThrowingController())
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    void illegalArgument_translatesTo400WithMessage() throws Exception {
        mockMvc.perform(get("/boom-illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("selection must be HOME or AWAY"));
    }
}
