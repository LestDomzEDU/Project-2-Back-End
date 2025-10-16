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

@RestController
class DummyController {
    @GetMapping("/rse")
    void throwRse() {
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Not Found Test");
    }
    @GetMapping("/empty")
    void throwEmpty() {
        throw new org.springframework.dao.EmptyResultDataAccessException(1);
    }
    @GetMapping("/illegal")
    void throwIllegal() {
        throw new IllegalArgumentException("Illegal argument test");
    }
}

public class ApiErrorHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DummyController())
                .setControllerAdvice(new ApiErrorHandler())
                .build();
    }

    @Test
    void testResponseStatusException() throws Exception {
        mockMvc.perform(get("/rse"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found Test"));
    }

    @Test
    void testEmptyResultDataAccessException() throws Exception {
        mockMvc.perform(get("/empty"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Resource not found for request"));
    }

    @Test
    void testIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Illegal argument test"));
    }
}
