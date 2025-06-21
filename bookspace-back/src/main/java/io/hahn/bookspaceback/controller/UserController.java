package io.hahn.bookspaceback.controller;

import io.hahn.bookspaceback.dto.UserDTO;
import io.hahn.bookspaceback.service.UserService;
import io.hahn.bookspaceback.util.PageWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody UserDTO userDTO) {
        UserDTO createdContent = userService.create(userDTO);
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        UserDTO updatedContent = userService.update(id, userDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        UserDTO content = userService.getById(id);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}