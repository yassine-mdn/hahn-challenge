package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.AuthenticationRequestDTO;
import io.hahn.bookspaceback.dto.AuthenticationResponseDTO;
import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private HttpServletRequest httpServletRequest;

    private UserDTO testUserDTO;
    private AuthenticationRequestDTO authRequestDTO;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        
        // Create a test user
        testUserDTO = new UserDTO();
        testUserDTO.setUserName("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setPassword("password123");
        testUserDTO.setRole(Role.USER);
        
        // Save the user to the database
        userService.create(testUserDTO);
        
        // Create authentication request
        authRequestDTO = new AuthenticationRequestDTO();
        authRequestDTO.setUsername("testuser");
        authRequestDTO.setPassword("password123");
    }

    @Test
    void authenticate_ShouldReturnAuthResponseDTO_WhenCredentialsAreValid() {
        var user = userRepository.findByUserNameOrEmail("testuser", "testuser").orElseThrow();
        AuthenticationResponseDTO response = authenticationService.authenticate(authRequestDTO);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals(Role.USER, response.getRole());
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void authenticate_ShouldThrowCustomException_WhenUserNotFound() {
        authRequestDTO.setUsername("nonexistentuser");

        CustomException exception = assertThrows(CustomException.class,
                () -> authenticationService.authenticate(authRequestDTO));
        
        assertEquals("User with username nonexistentuser not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void authenticate_ShouldThrowCustomException_WhenPasswordIsIncorrect() {
        authRequestDTO.setPassword("wrongpassword");

        assertThrows(CustomException.class,
                () -> authenticationService.authenticate(authRequestDTO));
    }

    @Test
    void refreshToken_ShouldReturnAuthResponseDTO_WhenRefreshTokenIsValid() {
        AuthenticationResponseDTO authResponse = authenticationService.authenticate(authRequestDTO);
        String refreshToken = authResponse.getRefreshToken();
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);

        AuthenticationResponseDTO response = authenticationService.refreshToken(httpServletRequest);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void refreshToken_ShouldThrowCustomException_WhenAuthHeaderIsMissing() {
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        CustomException exception = assertThrows(CustomException.class,
                () -> authenticationService.refreshToken(httpServletRequest));
        
        assertEquals("Request headers missing or invalid", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void refreshToken_ShouldThrowCustomException_WhenAuthHeaderDoesNotStartWithBearer() {
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Invalid token");

        CustomException exception = assertThrows(CustomException.class,
                () -> authenticationService.refreshToken(httpServletRequest));
        
        assertEquals("Request headers missing or invalid", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void refreshToken_ShouldThrowCustomException_WhenTokenIsInvalid() {
        String invalidToken = "invalid token";
        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + invalidToken);

        assertThrows(Exception.class, () -> authenticationService.refreshToken(httpServletRequest));
    }

    @Test
    void refreshToken_ShouldThrowCustomException_WhenUserNoLongerExists() {
        AuthenticationResponseDTO authResponse = authenticationService.authenticate(authRequestDTO);
        String refreshToken = authResponse.getRefreshToken();
        User user = userRepository.findByUserName("testuser").orElseThrow();
        userRepository.delete(user);

        when(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);

        CustomException exception = assertThrows(CustomException.class,
                () -> authenticationService.refreshToken(httpServletRequest));
        assertTrue(exception.getMessage().contains("not found"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}