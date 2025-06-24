package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.UserMapper;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO create(UserDTO userDTO) {
        try {
            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                log.error("Username {} already exists", userDTO.getUsername());
                throw new CustomException("Username already exists");
            }
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                log.error("Email {} already exists", userDTO.getEmail());
                throw new CustomException("Email already exists");
            }

            User saved = userMapper.toEntity(userDTO);
            saved.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            saved = userRepository.save(saved);
            return userMapper.toDTO(saved);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating user : {}", ex.getMessage(), ex);
            throw new CustomException("Failed to create user : " + ex);
        }
    }

    public UserDTO update(String id, UserDTO userDTO) {
        try {
            User updatedUser = userRepository.findById(id)
                    .map(user -> {
                        userMapper.updateUserFromDto(userDTO, user);
                        return user;
                    })
                    .orElseThrow(() -> {
                        log.error("User with id {} not found", id);
                        return new CustomException("User with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });

            updatedUser = userRepository.save(updatedUser);
            return userMapper.toDTO(updatedUser);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating user with id {} : {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to update user with id " + id + " : " + ex);
        }
    }
    
    //added last minute intead of modifying existing one because of tests
    public UserDTO updateByUsername(String username, @Valid @RequestBody UserDTO userDTO) {
        try {
            User updatedUser = userRepository.findByUsername(username)
                    .map(user -> {
                        userMapper.updateUserFromDto(userDTO, user);
                        return user;
                    })
                    .orElseThrow(() -> {
                        log.error("User with username {} not found", username);
                        return new CustomException("User with username " + username + " not found", HttpStatus.NOT_FOUND);
                    });

            updatedUser = userRepository.save(updatedUser);
            return userMapper.toDTO(updatedUser);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating user with username {} : {}", username, ex.getMessage(), ex);
            throw new CustomException("Failed to update user with username " + username + " : " + ex);
        } 
    }

    public UserDTO getById(String id) {
        try {
            return userRepository.findById(id)
                    .map(userMapper::toDTO)
                    .orElseThrow(() -> {
                        log.error("User with id {} not found", id);
                        return new CustomException("User with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching user with id {}: {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to fetch user with id " + id + " : " + ex);
        }
    }
    
    public UserDTO getByUsername(String username) {
        try {
            return userRepository.findByUsername(username)
                    .map(userMapper::toDTO)
                    .orElseThrow(() -> {
                        log.error("User with username {} not found", username);
                        return new CustomException("User with username " + username + " not found", HttpStatus.NOT_FOUND);
                    });
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching user with username {}: {}", username, ex.getMessage(), ex);
            throw new CustomException("Failed to fetch user with username " + username + " : " + ex);
        }
    }

    public PageWrapper<UserDTO> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(userRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(userMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching all users : {}", ex.getMessage());
            throw new CustomException("Failed to fetch all users : " + ex);
        }
    }

    public void delete(String username) {
        try {
            userRepository.deleteByUsername(username);
        } catch (Exception ex) {
            log.error("Error deleting user with username {} : {}", username, ex.getMessage());
            throw new CustomException("Failed to delete user with username " + username + " : " + ex);
        }
    }
}
