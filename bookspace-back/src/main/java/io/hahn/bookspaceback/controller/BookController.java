package io.hahn.bookspaceback.controller;


import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.service.BookService;
import io.hahn.bookspaceback.util.PageWrapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO bookDTO) {
        BookDTO createdContent = bookService.create(bookDTO);
        return ResponseEntity.ok(createdContent);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        BookDTO updatedContent = bookService.update(id, bookDTO);
        return ResponseEntity.ok(updatedContent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        BookDTO content = bookService.getById(id);
        return ResponseEntity.ok(content);
    }

    @GetMapping
    public ResponseEntity<PageWrapper<BookDTO>> getAll(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
            ) {
        PageWrapper<BookDTO> contentList = bookService.getAll(pageNumber, pageSize);
        return ResponseEntity.ok(contentList);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
