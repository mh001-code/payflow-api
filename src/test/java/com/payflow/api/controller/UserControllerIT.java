package com.payflow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.AbstractIntegrationTest;
import com.payflow.api.dto.CreateUserRequest;
import com.payflow.domain.model.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest validRequest() {
        return new CreateUserRequest(
                "João Silva",
                "12345678901",
                "joao@example.com",
                "senha123",
                UserType.COMUM
        );
    }

    @Test
    void postUsers_shouldReturn201_withUserData_whenValidRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("joao@example.com"))
                .andExpect(jsonPath("$.type").value("COMUM"));
    }

    @Test
    void postUsers_shouldReturn409_whenCpfAlreadyExists() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated());

        CreateUserRequest duplicate = new CreateUserRequest(
                "Outro Nome", "12345678901", "outro@example.com", "senha123", UserType.COMUM
        );
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("CPF")));
    }

    @Test
    void getUser_shouldReturn404_whenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/999999"))
                .andExpect(status().isNotFound());
    }
}
