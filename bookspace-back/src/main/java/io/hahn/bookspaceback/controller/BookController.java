package io.hahn.bookspaceback.controller;


import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.dto.RatingCountDTO;
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
        BookDTO createdBook = bookService.create(bookDTO);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.update(id, bookDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        BookDTO book = bookService.getById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<PageWrapper<BookDTO>> getAll(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        PageWrapper<BookDTO> bookList = bookService.getAll(search,pageNumber, pageSize);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/featured")
    public ResponseEntity<PageWrapper<BookDTO>> getAllFeatured(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ){
        PageWrapper<BookDTO> bookList = bookService.getFeatured(pageNumber, pageSize);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/popular")
    public ResponseEntity<PageWrapper<BookDTO>> getAllPopular(
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        PageWrapper<BookDTO> bookList = bookService.getPopular(pageNumber, pageSize);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{id}/similar")
    public ResponseEntity<PageWrapper<BookDTO>> getAllSimilar(
            @PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        PageWrapper<BookDTO> bookList = bookService.getSimilar(id, pageNumber, pageSize);
        return ResponseEntity.ok(bookList);
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<RatingCountDTO>> getAllRatings(@PathVariable Long id){
        List<RatingCountDTO> ratings = bookService.getRatingCountById(id);
        return ResponseEntity.ok(ratings);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
