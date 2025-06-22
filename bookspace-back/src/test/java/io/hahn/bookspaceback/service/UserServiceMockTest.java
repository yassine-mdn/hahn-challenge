package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.UserMapper;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User mockUserEntity;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId("test-id");
        userDTO.setUserName("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        userDTO.setCreatedAt(LocalDateTime.now());

        mockUserEntity = new User();
        ReflectionTestUtils.setField(mockUserEntity, "id", "test-id");
        ReflectionTestUtils.setField(mockUserEntity, "userName", "testuser");
        ReflectionTestUtils.setField(mockUserEntity, "email", "test@example.com");
        ReflectionTestUtils.setField(mockUserEntity, "password", "password123");
        ReflectionTestUtils.setField(mockUserEntity, "role", Role.USER);
        ReflectionTestUtils.setField(mockUserEntity, "createdAt", LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnUserDTO_WhenUserIsCreatedSuccessfully() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(mockUserEntity);
        when(userMapper.toDTO(mockUserEntity)).thenReturn(userDTO);
        when(userMapper.toEntity(userDTO)).thenReturn(mockUserEntity);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(userDTO.getPassword());

        UserDTO result = userService.create(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUserName(), result.getUserName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPassword(), result.getPassword());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    void create_ShouldThrowCustomException_WhenUsernameAlreadyExists() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.of(mockUserEntity));

        CustomException exception = assertThrows(CustomException.class, () -> userService.create(userDTO));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void create_ShouldThrowCustomException_WhenEmailAlreadyExists() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUserEntity));

        CustomException exception = assertThrows(CustomException.class, () -> userService.create(userDTO));

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void create_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(userRepository.findByUserName(userDTO.getUserName())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
        when(userMapper.toEntity(userDTO)).thenReturn(mockUserEntity);

        CustomException exception = assertThrows(CustomException.class, () -> userService.create(userDTO));

        assertEquals("Failed to create user : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void update_ShouldReturnUserDTO_WhenUserIsUpdatedSuccessfully() {
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(mockUserEntity));
        when(userRepository.save(mockUserEntity)).thenReturn(mockUserEntity);
        when(userMapper.toDTO(mockUserEntity)).thenReturn(userDTO);
        doAnswer((answer) -> {
            ReflectionTestUtils.setField(mockUserEntity, "password", userDTO.getPassword());
            return null;
        }).when(userMapper).updateUserFromDto(userDTO, mockUserEntity);

        UserDTO result = userService.update(userDTO.getId(), userDTO);

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUserName(), result.getUserName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPassword(), result.getPassword());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    void update_ShouldThrowCustomException_WhenUserNotFound() {
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.update(userDTO.getId(), userDTO));

        assertEquals("User with id " + userDTO.getId() + " not found", exception.getMessage());
    }

    @Test
    void update_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(userRepository.findById(userDTO.getId())).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> userService.update(userDTO.getId(), userDTO));

        assertEquals("Failed to update user with id " + userDTO.getId() + " : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnUserDTO_WhenUserIsFound() {
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.of(mockUserEntity));
        when(userMapper.toDTO(mockUserEntity)).thenReturn(userDTO);

        UserDTO result = userService.getById(userDTO.getId());

        assertNotNull(result);
        assertEquals(userDTO.getId(), result.getId());
        assertEquals(userDTO.getUserName(), result.getUserName());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getPassword(), result.getPassword());
        assertEquals(userDTO.getRole(), result.getRole());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenUserNotFound() {
        when(userRepository.findById(userDTO.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.getById(userDTO.getId()));

        assertEquals("User with id " + userDTO.getId() + " not found", exception.getMessage());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(userRepository.findById(userDTO.getId())).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> userService.getById(userDTO.getId()));

        assertEquals("Failed to fetch user with id " + userDTO.getId() + " : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void getAll_ShouldReturnUserDTO_WhenUsersExist() {
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(mockUserEntity)));
        when(userMapper.toDTO(mockUserEntity)).thenReturn(userDTO);

        PageWrapper<UserDTO> result = userService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(userDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoUsersFound() {
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));

        PageWrapper<UserDTO> result = userService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageNumberIsNegative() {
        CustomException exception = assertThrows(CustomException.class, () -> userService.getAll(-1, 10));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsNegative() {
        CustomException exception = assertThrows(CustomException.class, () -> userService.getAll(0, -1));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsZero() {
        CustomException exception = assertThrows(CustomException.class, () -> userService.getAll(0, 0));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(userRepository.findAll(PageRequest.of(0, 10))).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> userService.getAll(0, 10));

        assertEquals("Failed to fetch all users : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void delete_ShouldDeleteUser_WhenUserExists() {
        doNothing().when(userRepository).deleteByUserName(userDTO.getUserName());

        userService.delete(userDTO.getUserName());

        verify(userRepository, times(1)).deleteByUserName(userDTO.getUserName());
    }

    @Test
    void delete_ShouldThrowCustomException_WhenExceptionOccurs() {
        doThrow(new RuntimeException("Database error")).when(userRepository).deleteByUserName(userDTO.getUserName());

        CustomException exception = assertThrows(CustomException.class, () -> userService.delete(userDTO.getUserName()));

        assertEquals("Failed to delete user with username " + userDTO.getUserName() + " : java.lang.RuntimeException: Database error", exception.getMessage());
    }
}