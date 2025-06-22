package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.AuthenticationRequestDTO;
import io.hahn.bookspaceback.dto.AuthenticationResponseDTO;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.security.JwtService;
import io.hahn.bookspaceback.security.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authRequestDTO) {
        try{
            SecurityUser user = userRepository.findByUserNameOrEmail(authRequestDTO.getUsername(), authRequestDTO.getUsername()).map(SecurityUser::new)
                    .orElseThrow(() -> {
                        log.error("User with username {} not found", authRequestDTO.getUsername());
                        return new CustomException("User with username " + authRequestDTO.getUsername() + " not found", HttpStatus.NOT_FOUND);
                    });
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getUsername(),
                            authRequestDTO.getPassword()
                    )
            );
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            return AuthenticationResponseDTO.builder()
                    .username(user.getUsername())
                    .role(user.getUser().getRole())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new CustomException(ex.getMessage());
        }
    }


    public AuthenticationResponseDTO refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("Request headers missing or invalid");
            throw new CustomException("Request headers missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username == null || username.isEmpty()) {
            log.error("Request headers invalid");
            throw new CustomException("Request headers invalid", HttpStatus.UNAUTHORIZED);
        }
        SecurityUser user = userRepository.findByUserNameOrEmail(username, username).map(SecurityUser::new)
                .orElseThrow(() -> {
                    log.error("User with username or email '{}' not found during refresh token process", username);
                    return new CustomException("User with username or email '" + username + "' not found while processing refresh token", HttpStatus.NOT_FOUND);
                });
        if (!jwtService.isTokenValid(refreshToken, user)) {
            log.error("Refresh Token invalid");
            throw new CustomException("Refresh Token invalid", HttpStatus.UNAUTHORIZED);
        }
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}