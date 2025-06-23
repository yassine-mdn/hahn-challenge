package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.UserMapper;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userService = new UserService(userRepository, userMapper, passwordEncoder);
    }

    private UserDTO generateUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        return userDTO;
    }

    @Test
    void create_ShouldReturnUserDTO_WhenUserIsCreatedSuccessfully() {
        UserDTO userDTO = generateUserDTO();

        UserDTO result = userService.create(userDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    void create_ShouldThrowCustomException_WhenUsernameAlreadyExists() {
        UserDTO userDTO = generateUserDTO();
        userService.create(userDTO);
        
        UserDTO duplicateUser = generateUserDTO();
        duplicateUser.setEmail("different@example.com");

        CustomException exception = assertThrows(CustomException.class, () -> userService.create(duplicateUser));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void create_ShouldThrowCustomException_WhenEmailAlreadyExists() {
        UserDTO userDTO = generateUserDTO();
        userService.create(userDTO);
        
        UserDTO duplicateUser = generateUserDTO();
        duplicateUser.setUsername("differentuser");

        CustomException exception = assertThrows(CustomException.class, () -> userService.create(duplicateUser));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void update_ShouldReturnUserDTO_WhenUserIsUpdatedSuccessfully() {
        UserDTO userDTO = generateUserDTO();
        UserDTO savedUser = userService.create(userDTO);
        
        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setPassword("newpassword123");
        
        UserDTO result = userService.update(savedUser.getId(), updatedUserDTO);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals("newpassword123", result.getPassword());
        assertEquals(savedUser.getRole(), result.getRole());
    }

    @Test
    void update_ShouldThrowCustomException_WhenUserNotFound() {
        UserDTO userDTO = generateUserDTO();
        
        CustomException exception = assertThrows(CustomException.class, 
            () -> userService.update("non-existent-id", userDTO));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getById_ShouldReturnUserDTO_WhenUserExists() {
        UserDTO userDTO = generateUserDTO();
        UserDTO savedUser = userService.create(userDTO);

        UserDTO result = userService.getById(savedUser.getId());

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getUsername(), result.getUsername());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getPassword(), result.getPassword());
        assertEquals(savedUser.getRole(), result.getRole());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenUserNotFound() {
        CustomException exception = assertThrows(CustomException.class, 
            () -> userService.getById("non-existent-id"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getAll_ShouldReturnPageOfUserDTO_WhenUsersExist() {
        UserDTO userDTO = generateUserDTO();
        userService.create(userDTO);
        
        UserDTO secondUser = generateUserDTO();
        secondUser.setUsername("seconduser");
        secondUser.setEmail("second@example.com");
        userService.create(secondUser);

        PageWrapper<UserDTO> result = userService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenInvalidPaginationParameters() {
        CustomException exception = assertThrows(CustomException.class, 
            () -> userService.getAll(-1, 10));

        assertEquals("Invalid pagination parameters", exception.getMessage());
        
        exception = assertThrows(CustomException.class, 
            () -> userService.getAll(0, 0));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void delete_ShouldRemoveUserFromDB_WhenDeleteIsSuccessful() {
        UserDTO userDTO = generateUserDTO();
        UserDTO savedUser = userService.create(userDTO);

        userService.delete(savedUser.getUsername());

        CustomException exception = assertThrows(CustomException.class, 
            () -> userService.getByUsername(savedUser.getUsername()));
        
        assertTrue(exception.getMessage().contains("not found"));
    }
}