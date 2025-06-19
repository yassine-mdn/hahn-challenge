package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.enums.Genre;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.BookMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceMockTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book mockBookEntity;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("test book");
        bookDTO.setAuthor("test author");
        bookDTO.setPublisher("test publisher");
        bookDTO.setGenres(List.of(Genre.NON_FICTION, Genre.BIOGRAPHY));
        bookDTO.setDescription("test description");
        bookDTO.setCoverUrl("test cover url");

        mockBookEntity = new Book();
        ReflectionTestUtils.setField(bookDTO, "id", 1L);
        ReflectionTestUtils.setField(bookDTO, "title", "test book");
        ReflectionTestUtils.setField(bookDTO, "author", "test author");
        ReflectionTestUtils.setField(bookDTO, "publisher", "test publisher");
        ReflectionTestUtils.setField(bookDTO, "genres", List.of(Genre.NON_FICTION, Genre.BIOGRAPHY));
        ReflectionTestUtils.setField(bookDTO, "description", "test description");
        ReflectionTestUtils.setField(bookDTO, "coverUrl", "test cover url");
    }


    @Test
    void create_ShouldReturnBookDTO_WhenBookIsCreatedSuccessfully() {
        when(bookRepository.save(mockBookEntity)).thenReturn(mockBookEntity);
        when(bookMapper.toDTO(mockBookEntity)).thenReturn(bookDTO);
        when(bookMapper.toEntity(bookDTO)).thenReturn(mockBookEntity);

        BookDTO results = bookService.create(bookDTO);

        assertNotNull(results);
        assertEquals(results.getId(), bookDTO.getId());
        assertEquals(results.getTitle(), bookDTO.getTitle());
        assertEquals(results.getAuthor(), bookDTO.getAuthor());
        assertEquals(results.getPublisher(), bookDTO.getPublisher());
        assertEquals(results.getGenres(), bookDTO.getGenres());
        assertEquals(results.getDescription(), bookDTO.getDescription());
        assertEquals(results.getCoverUrl(), bookDTO.getCoverUrl());
    }

    @Test
    void create_ShouldThrowCustomException_WhenBookAlreadyExists() {
        when(bookRepository.existsById(bookDTO.getId())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> bookService.create(bookDTO));

        assertEquals("book with id 1 already exists", exception.getMessage());
    }

    @Test
    void create_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(bookRepository.save(mockBookEntity)).thenThrow(new RuntimeException("Database error"));
        when(bookMapper.toEntity(bookDTO)).thenReturn(mockBookEntity);

        CustomException exception = assertThrows(CustomException.class, () -> bookService.create(bookDTO));

        assertEquals("Failed to create book : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void update_ShouldReturnBookDTO_WhenBookIsUpdatedSuccessfully() {
        bookDTO.setTitle("updated title");
        when(bookRepository.findById(bookDTO.getId())).thenReturn(Optional.of(mockBookEntity));
        when(bookRepository.save(mockBookEntity)).thenReturn(mockBookEntity);
        when(bookMapper.toDTO(mockBookEntity)).thenReturn(bookDTO);
        doAnswer((answer) -> {
            ReflectionTestUtils.setField(mockBookEntity, "title", bookDTO.getTitle());
            return null;
        }).when(bookMapper).updateBookFromDto(bookDTO,mockBookEntity);

        BookDTO results = bookService.update(bookDTO.getId(),bookDTO);

        assertNotNull(results);
        assertEquals(results.getId(), bookDTO.getId());
        assertEquals(results.getTitle(), bookDTO.getTitle());
    }

    @Test
    void update_ShouldThrowCustomException_WhenBookNotFound() {
        when(bookRepository.findById(bookDTO.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.update(bookDTO.getId(),bookDTO));

        assertEquals("Book with id " + bookDTO.getId() + " not found", exception.getMessage());
    }

    @Test
    void update_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(bookRepository.findById(bookDTO.getId())).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> bookService.update(bookDTO.getId(),bookDTO));

        assertEquals("Failed to update book with id " + bookDTO.getId() + " : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void getById_ShouldReturnBookDTO_WhenBookIsFound() {
        when(bookRepository.findById(bookDTO.getId())).thenReturn(Optional.of(mockBookEntity));
        when(bookMapper.toDTO(mockBookEntity)).thenReturn(bookDTO);

        BookDTO results = bookService.getById(bookDTO.getId());

        assertNotNull(results);
        assertEquals(results.getId(), bookDTO.getId());
        assertEquals(results.getTitle(), bookDTO.getTitle());
        assertEquals(results.getAuthor(), bookDTO.getAuthor());
        assertEquals(results.getPublisher(), bookDTO.getPublisher());
        assertEquals(results.getGenres(), bookDTO.getGenres());
        assertEquals(results.getDescription(), bookDTO.getDescription());
        assertEquals(results.getCoverUrl(), bookDTO.getCoverUrl());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenBookNotFound() {
        when(bookRepository.findById(bookDTO.getId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.getById(bookDTO.getId()));

        assertEquals("Book with id " + bookDTO.getId() + " not found", exception.getMessage());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(bookRepository.findById(bookDTO.getId())).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> bookService.getById(bookDTO.getId()));

        assertEquals("Failed to fetch book with id " + bookDTO.getId() + " : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void getAll_ShouldReturnBookDTO_WhenBookIsFound() {
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of(mockBookEntity)));
        when(bookMapper.toDTO(mockBookEntity)).thenReturn(bookDTO);

        Page<BookDTO> results = bookService.getAll(0, 10);

        assertNotNull(results);
        assertEquals(1, results.getContent().size());
        assertEquals(results.getContent().get(0).getId(), bookDTO.getId());
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoBooksFound() {
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(List.of()));

        Page<BookDTO> results = bookService.getAll(0, 10);

        assertNotNull(results);
        assertEquals(0, results.getContent().size());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageNumberIsNegative() {

        CustomException exception = assertThrows(CustomException.class, () -> bookService.getAll(-1, 1));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsNegative() {
        CustomException exception = assertThrows(CustomException.class, () -> bookService.getAll(0, -1));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsZero() {
        CustomException exception = assertThrows(CustomException.class, () -> bookService.getAll(0, 0));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(bookRepository.findAll(PageRequest.of(0, 10))).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> bookService.getAll(0, 10));

        assertEquals("Failed to fetch all books : java.lang.RuntimeException: Database error", exception.getMessage());
    }

    @Test
    void delete_ShouldDeleteBook_WhenBookExists() {
        when(bookRepository.findById(bookDTO.getId())).thenReturn(Optional.empty());

        bookService.delete(bookDTO.getId());

        assertTrue(bookRepository.findById(bookDTO.getId()).isEmpty());
    }
}