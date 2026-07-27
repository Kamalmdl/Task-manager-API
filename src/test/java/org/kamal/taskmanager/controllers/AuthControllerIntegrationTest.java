package org.kamal.taskmanager.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_shouldReturn200_whenDataIsValid() throws Exception {
        String requestBody = """
                {
                    "name": "Anya",
                    "email": "anya@example.com",
                    "password": "qwerty123"
                }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("anya@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void register_shouldReturn400_whenEmailIsNotValid() throws Exception {
        String requestBody = """
                        {
                            "name": "Anya",
                            "email":"anyagmail.com",
                            "password":"qwerty123"
                        }
                """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("email: Email must be valid"));
    }

    @Test
    void createBoard_shouldSucceed_whenUserIsAuthenticated() throws Exception {
        String registerBody = """
                {
                    "name": "Ilya",
                    "email": "ilya@example.com",
                    "password": "qwerty123"
                }
                """;
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isOk());

        String loginBody = """
                {
                    "email":"ilya@example.com",
                    "password":"qwerty123"
                }
                """;

        String responseBody = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(responseBody);
        String token = json.get("token").asText();

        String boardBody = """
        {
            "name": "My Board",
            "description": "Test board"
        }
        """;

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(boardBody)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("My Board"))
                .andExpect(jsonPath("$.description").value("Test board"));
    }
}