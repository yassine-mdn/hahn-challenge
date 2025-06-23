package io.hahn.bookspaceback.controller;

import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.dto.ReviewRequestDTO;
import io.hahn.bookspaceback.service.ReviewService;
import io.hahn.bookspaceback.util.PageWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewRequestDTO reviewRequestDTO, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        ReviewDTO reviewDTO = reviewService.secureCreate(principal.getName(), reviewRequestDTO);
        return ResponseEntity.ok(reviewDTO);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<PageWrapper<ReviewDTO>> get(
            @PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        PageWrapper<ReviewDTO> reviewList = reviewService.getAllByBookId(id, pageNumber, pageSize);
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<PageWrapper<ReviewDTO>> getByUsername(
            @PathVariable String username,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer pageSize
    ) {
        PageWrapper<ReviewDTO> reviewList = reviewService.getAllByUsername(username, pageNumber, pageSize);
        return ResponseEntity.ok(reviewList);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(' ADMIN')")
    public ResponseEntity<ReviewDTO> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
