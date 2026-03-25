package com.vinh.financetracker.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Nó sẽ tìm file application-test.yml để chạy
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc; // Công cụ giả lập gọi API mà không cần chạy server thật

    @Test
    void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk()) // Mong đợi kết quả trả về là 200 OK
                .andExpect(jsonPath("$.status").value("UP")); // Mong đợi JSON có field status = UP
    }
}
