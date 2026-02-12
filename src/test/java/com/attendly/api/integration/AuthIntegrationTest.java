package com.attendly.api.integration;

import com.attendly.api.modules.users.Role;
import com.attendly.api.modules.users.User;
import com.attendly.api.modules.users.UserRepository;
import com.attendly.api.modules.users.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String jwtToken;

    @BeforeEach
    void setUp() {
        if (!userRepository.existsByEmail("test@attendly.com")) {
            User admin = User.builder()
                    .fullName("Test Admin")
                    .email("test@attendly.com")
                    .passwordHash(passwordEncoder.encode("password123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    @Test
    @Order(1)
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@attendly.com", "password123");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.email").value("test@attendly.com"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"))
                .andReturn();

        // Extract token for subsequent tests
        String responseBody = result.getResponse().getContentAsString();
        jwtToken = objectMapper.readTree(responseBody).get("data").get("token").asText();
    }

    @Test
    @Order(2)
    void shouldRejectInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@attendly.com", "wrongpassword");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void shouldGetCurrentUser() throws Exception {
        // First login to get token
        if (jwtToken == null) {
            shouldLoginSuccessfully();
        }

        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("test@attendly.com"))
                .andExpect(jsonPath("$.data.fullName").value("Test Admin"));
    }

    @Test
    @Order(4)
    void shouldRejectUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(5)
    void healthEndpointShouldBePublic() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}
