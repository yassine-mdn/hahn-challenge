package io.hahn.bookspaceback.controller;

import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.service.ReadingListService;
import io.hahn.bookspaceback.util.PageWrapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users/{username}/reading-list")
public class ReadingListController {
    
    private ReadingListService readingListService;

    @PostMapping
    public ResponseEntity<ReadingListDTO> create(@PathVariable(name = "username") String username,@RequestBody @Valid ReadingListRequestDTO readingListRequestDTO) {
        ReadingListDTO createdContent = readingListService.create(readingListRequestDTO);
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadingListDTO> update(@PathVariable(name = "username") String username,@PathVariable(name = "id") Long id, @Valid @RequestBody ReadingListRequestDTO readingListRequestDTO) {
        // TODO: Check ownership
        ReadingListDTO updatedContent = readingListService.update(id, readingListRequestDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadingListDTO> getById(@PathVariable(name = "id") Long id) {
        ReadingListDTO content = readingListService.getById(id);
        return ResponseEntity.ok(content);
    }

    @GetMapping
    public ResponseEntity<PageWrapper<ReadingListDTO>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize,
            @PathVariable(name = "username") String username
    ) {
        PageWrapper<ReadingListDTO> contentList = readingListService.getAllByUser(username,pageNumber, pageSize);
        return ResponseEntity.ok(contentList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        // TODO: check ownership
        readingListService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
