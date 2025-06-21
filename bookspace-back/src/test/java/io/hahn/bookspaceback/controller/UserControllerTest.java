package io.hahn.bookspaceback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.exception.GlobalExceptionHandler;
import io.hahn.bookspaceback.service.UserService;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private UserDTO mockUserDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Get the validator from WebMvcConfigurationSupport
        Validator validator = new WebMvcConfigurationSupport().mvcValidator();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        mockUserDTO = new UserDTO();
        mockUserDTO.setId("test-uuid");
        mockUserDTO.setUserName("testuser");
        mockUserDTO.setEmail("test@example.com");
        mockUserDTO.setPassword("password123");
        mockUserDTO.setRole(Role.USER);
        mockUserDTO.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnCreatedUserDTO_WhenSuccess() throws Exception {
        when(userService.create(any(UserDTO.class))).thenReturn(mockUserDTO);

        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);
        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-uuid"))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        when(userService.create(any(UserDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenUserNameIsLong() throws Exception {
        mockUserDTO.setUserName("user name with more than 15 characters");
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userName").value("Username must be between 3 and 15 characters"));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenUserNameHasInvalidCharacters() throws Exception {
        mockUserDTO.setUserName("ya$$ine");
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userName").value("Username must contain only alphanumeric characters and hyphens (-)"));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenEmailIsInvalid() throws Exception {
        mockUserDTO.setEmail("invalid-email");
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenPasswordIsShort() throws Exception {
        mockUserDTO.setPassword("short");
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").value("Password must be between 6 and 20 characters"));
    }

    @Test
    void create_ShouldReturnBadRequest_InvalidJson() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/api/v1/users")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnUpdatedUserDTO_WhenSuccess() throws Exception {
        String id = "test-uuid";
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        when(userService.update(eq(id), any(UserDTO.class))).thenReturn(mockUserDTO);

        mockMvc.perform(put("/api/v1/users/" + id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-uuid"))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String id = "test-uuid";
        String jsonRequest = objectMapper.writeValueAsString(mockUserDTO);

        when(userService.update(eq(id), any(UserDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(put("/api/v1/users/" + id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnBadRequest_InvalidJson() throws Exception {
        String id = "test-uuid";
        String invalidJson = "{invalid json}";

        mockMvc.perform(put("/api/v1/users/" + id)
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_ShouldReturnUserDTO_WhenSuccess() throws Exception {
        String id = "test-uuid";

        when(userService.getById(id)).thenReturn(mockUserDTO);

        mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-uuid"))
                .andExpect(jsonPath("$.userName").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getById_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String id = "test-uuid";

        when(userService.getById(id)).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/users/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_ShouldReturnPageOfUserDTOs_WhenSuccess() throws Exception {
        PageWrapper<UserDTO> userPage = new PageWrapper<>(List.of(mockUserDTO));

        when(userService.getAll(0, 10)).thenReturn(userPage);

        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("test-uuid"))
                .andExpect(jsonPath("$.content[0].userName").value("testuser"))
                .andExpect(jsonPath("$.content[0].email").value("test@example.com"))
                .andExpect(jsonPath("$.content[0].password").value("password123"))
                .andExpect(jsonPath("$.content[0].role").value("USER"));
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoUsers() throws Exception {
        PageWrapper<UserDTO> emptyPage = new PageWrapper<>(List.of());

        when(userService.getAll(0, 10)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/users?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getAll_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        when(userService.getAll(0, 10)).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_ShouldReturnNoContent_WhenSuccess() throws Exception {
        String id = "test-uuid";

        mockMvc.perform(delete("/api/v1/users/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String id = "test-uuid";

        doThrow(new CustomException("Error occurred")).when(userService).delete(id);

        mockMvc.perform(delete("/api/v1/users/" + id))
                .andExpect(status().isBadRequest());
    }
}
