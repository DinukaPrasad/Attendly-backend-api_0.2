package com.attendly.api.integration;

import com.attendly.api.modules.attendance.dto.MarkAttendanceRequest;
import com.attendly.api.modules.enrolments.ModuleEnrolment;
import com.attendly.api.modules.enrolments.ModuleEnrolmentRepository;
import com.attendly.api.modules.modules.Module;
import com.attendly.api.modules.modules.ModuleRepository;
import com.attendly.api.modules.sessions.Session;
import com.attendly.api.modules.sessions.SessionRepository;
import com.attendly.api.modules.sessions.SessionStatus;
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

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AttendanceIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private ModuleEnrolmentRepository enrolmentRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private static String studentToken;
    private static Long studentId;
    private static Long sessionId;

    @BeforeEach
    void setUp() throws Exception {
        if (studentToken != null) return;

        // Create lecturer
        User lecturer = userRepository.save(User.builder()
                .fullName("Dr. Smith")
                .email("lecturer@attendly.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(Role.LECTURER)
                .build());

        // Create student
        User student = userRepository.save(User.builder()
                .fullName("John Student")
                .email("student@attendly.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(Role.STUDENT)
                .build());
        studentId = student.getId();

        // Create module
        Module module = moduleRepository.save(Module.builder()
                .code("CS101")
                .name("Intro to CS")
                .level((short) 1)
                .build());

        // Enrol student
        enrolmentRepository.save(ModuleEnrolment.builder()
                .module(module)
                .student(student)
                .build());

        // Create open session
        Session session = sessionRepository.save(Session.builder()
                .module(module)
                .lecturer(lecturer)
                .title("Lecture 1")
                .startTime(LocalDateTime.now().minusHours(1))
                .endTime(LocalDateTime.now().plusHours(1))
                .status(SessionStatus.OPEN)
                .attendanceCode("ABC123")
                .build());
        sessionId = session.getId();

        // Login as student
        LoginRequest loginRequest = new LoginRequest("student@attendly.com", "password123");
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        studentToken = objectMapper.readTree(result.getResponse().getContentAsString())
                .get("data").get("token").asText();
    }

    @Test
    @Order(1)
    void shouldMarkAttendanceSuccessfully() throws Exception {
        MarkAttendanceRequest request = new MarkAttendanceRequest();
        request.setSessionId(sessionId);
        request.setStudentId(studentId);
        request.setAttendanceCode("ABC123");
        request.setNote("Arrived on time");

        mockMvc.perform(post("/api/v1/attendance/mark")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("PRESENT"))
                .andExpect(jsonPath("$.data.studentFullName").value("John Student"));
    }

    @Test
    @Order(2)
    void shouldRejectDuplicateAttendance() throws Exception {
        MarkAttendanceRequest request = new MarkAttendanceRequest();
        request.setSessionId(sessionId);
        request.setStudentId(studentId);
        request.setAttendanceCode("ABC123");

        mockMvc.perform(post("/api/v1/attendance/mark")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(3)
    void shouldRejectWrongAttendanceCode() throws Exception {
        // Create another student for this test
        User student2 = userRepository.save(User.builder()
                .fullName("Jane Student")
                .email("student2@attendly.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(Role.STUDENT)
                .build());

        // Enrol student2 in module
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        enrolmentRepository.save(ModuleEnrolment.builder()
                .module(session.getModule())
                .student(student2)
                .build());

        MarkAttendanceRequest request = new MarkAttendanceRequest();
        request.setSessionId(sessionId);
        request.setStudentId(student2.getId());
        request.setAttendanceCode("WRONG");

        mockMvc.perform(post("/api/v1/attendance/mark")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
