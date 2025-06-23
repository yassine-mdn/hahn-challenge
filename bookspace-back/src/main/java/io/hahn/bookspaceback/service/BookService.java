package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.BookMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import io.hahn.bookspaceback.specification.BookSpec;
import io.hahn.bookspaceback.util.PageWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDTO create(BookDTO bookDTO) {
        try {
            if (bookDTO.getId() != null && bookRepository.existsById(bookDTO.getId())) {
                log.error("book with id {} already exists", bookDTO.getId());
                throw new CustomException("book with id " + bookDTO.getId() + " already exists");
            }
            Book saved = bookMapper.toEntity(bookDTO);
            saved = bookRepository.save(saved);
            return bookMapper.toDTO(saved);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error creating book : {}", ex.getMessage(), ex);
            throw new CustomException("Failed to create book : " + ex);
        }
    }


    public BookDTO update(Long id, BookDTO bookDTO) {
        try {
            Book updatedBook = bookRepository.findById(id)
                    .map(book -> {
                        bookMapper.updateBookFromDto(bookDTO, book);
                        return book;
                    })
                    .orElseThrow(() -> {
                        log.error("Book with id {} not found", id);
                        return new CustomException("Book with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });

            updatedBook = bookRepository.save(updatedBook);
            return bookMapper.toDTO(updatedBook);
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error updating book with id {} : {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to update book with id " + id + " : " + ex);
        }
    }

    public BookDTO getById(Long id) {
        try {
            return bookRepository.findById(id)
                    .map(bookMapper::toDTO)
                    .orElseThrow(() -> {
                        log.error("Book with id {} not found", id);
                        return new CustomException("Book with id " + id + " not found", HttpStatus.NOT_FOUND);
                    });
        } catch (CustomException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error fetching book with id {}: {}", id, ex.getMessage(), ex);
            throw new CustomException("Failed to fetch book with id " + id + " : " + ex);
        }
    }

    public PageWrapper<BookDTO> getAll(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(bookRepository.findAll(PageRequest.of(pageNumber, pageSize)).map(bookMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching all books : {}", ex.getMessage());
            throw new CustomException("Failed to fetch all books : " + ex);
        }
    }

    public PageWrapper<BookDTO> getAll(String search, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(bookRepository.findAll(BookSpec.search(search), PageRequest.of(pageNumber, pageSize)).map(bookMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching all books : {}", ex.getMessage());
            throw new CustomException("Failed to fetch all books : " + ex);
        }
    }

    public PageWrapper<BookDTO> getPopular(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            // could be dynamic as a param (1D, 1W, 1M, 1Y)
            LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
            return new PageWrapper<>(bookRepository.findAllByPopularity(oneDayAgo, PageRequest.of(pageNumber, pageSize)).map(bookMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching popular books : {}", ex.getMessage());
            throw new CustomException("Failed to fetch popular books : " + ex);
        }
    }

    public PageWrapper<BookDTO> getFeatured(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            return new PageWrapper<>(bookRepository.findAllByIsFeaturedTrue(PageRequest.of(pageNumber, pageSize)).map(bookMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching featured books : {}", ex.getMessage());
            throw new CustomException("Failed to fetch featured books : " + ex);
        }
    }

    public PageWrapper<BookDTO> getSimilar(Long referenceBookId, int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new CustomException("Invalid pagination parameters");
        }
        try {
            BookDTO referenceBook = getById(referenceBookId);
            return new PageWrapper<>(bookRepository.findSimilarBooks(
                            referenceBookId,
                            referenceBook.getAuthor(),
                            referenceBook.getGenres(),
                            PageRequest.of(pageNumber, pageSize)
                    ).map(bookMapper::toDTO));
        } catch (Exception ex) {
            log.error("Error fetching similar books : {}", ex.getMessage());
            throw new CustomException("Failed to fetch similar books : " + ex);
        }
    }

    public void delete(Long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception ex) {
            log.error("Error deleting book with id {} : {}", id, ex.getMessage());
            throw new CustomException("Failed to delete book with id " + id + " : " + ex);
        }
    }

}
