package io.hahn.bookspaceback.controller;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.service.UserService;
import io.hahn.bookspaceback.util.PageWrapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole(' ADMIN')")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdContent = userService.create(userDTO);
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/{username}")
    @PreAuthorize("#username == principal.user.userName or hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> update(@PathVariable String username, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedContent = userService.update(username, userDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getById(@PathVariable String username) {
        UserDTO content = userService.getById(username);
        return ResponseEntity.ok(content);
    }

    @GetMapping
    public ResponseEntity<PageWrapper<UserDTO>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
            ) {
        PageWrapper<UserDTO> contentList = userService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(contentList);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("#username == principal.user.userName or hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }
}