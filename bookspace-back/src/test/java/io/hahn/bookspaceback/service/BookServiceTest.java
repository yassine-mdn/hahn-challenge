package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.enums.Genre;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.BookMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    private BookService bookService;


    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        bookService = new BookService(bookRepository, bookMapper);
    }

    private BookDTO generateBookDTO() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("test book");
        bookDTO.setAuthor("test author");
        bookDTO.setPublisher("test publisher");
        bookDTO.setGenres(List.of(Genre.NON_FICTION,Genre.BIOGRAPHY));
        bookDTO.setDescription("test description");
        bookDTO.setCoverUrl("test cover url");
        return bookDTO;
    }

    @Test
    void create_ShouldReturnBookDTO_WhenBookIsCreatedSuccessfully() {

        BookDTO bookDTO = generateBookDTO();

        BookDTO result = bookService.create(bookDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(1L, result.getId());
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getPublisher(), result.getPublisher());
        assertEquals(bookDTO.getGenres(), result.getGenres());
        assertEquals(bookDTO.getDescription(), result.getDescription());
        assertEquals(bookDTO.getCoverUrl(), result.getCoverUrl());
        assertNull(bookDTO.getIsFeatured());
        assertFalse(result.getIsFeatured());

    }

    @Test
    void create_ShouldThrowCustomException_WhenBookAlreadyExists() {
        BookDTO bookDTO = generateBookDTO();
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);
        bookDTO.setId(book.getId());

        CustomException exception = assertThrows(CustomException.class, () -> bookService.create(bookDTO));

        assertEquals("book with id 1 already exists", exception.getMessage());

    }

    @Test
    void create_ShouldThrowCustomException_WhenBookDTOHasAnId() {
        BookDTO bookDTO = generateBookDTO();
        bookDTO.setId(1L);

        assertThrows(CustomException.class, () -> bookService.create(bookDTO));
    }

    @Test
    void update_ShouldReturnBookDTO_WhenBookIsUpdatedSuccessfully() {
        BookDTO bookDTO = generateBookDTO();
        Book updatedBook = bookMapper.toEntity(bookDTO);
        Book result = bookRepository.save(updatedBook);
        BookDTO newBookDTO = generateBookDTO();
        newBookDTO.setTitle("new title");

        BookDTO results = bookService.update(result.getId(), newBookDTO);

        assertNotNull(results);
        assertEquals(newBookDTO.getTitle(), results.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getPublisher(), result.getPublisher());
        assertEquals(bookDTO.getGenres(), result.getGenres());
        assertEquals(bookDTO.getDescription(), result.getDescription());
        assertEquals(bookDTO.getCoverUrl(), result.getCoverUrl());
    }

    @Test
    void getById_ShouldReturnBookDTO_WhenBookExists() {

        BookDTO bookDTO = generateBookDTO();
        Book book = bookMapper.toEntity(bookDTO);

        book = bookRepository.save(book);


        BookDTO result = bookService.getById(book.getId());


        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getPublisher(), result.getPublisher());
        assertEquals(bookDTO.getGenres(), result.getGenres());
        assertEquals(bookDTO.getDescription(), result.getDescription());
        assertEquals(bookDTO.getCoverUrl(), result.getCoverUrl());
    }

    @Test
    void getAll_ShouldReturnPageOfBookDTO_WhenBooksExist() {
        BookDTO bookDTO = generateBookDTO();
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);

        Page<BookDTO> result = bookService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(book.getId(), result.getContent().get(0).getId());
    }

    @Test
    void deleteById_ShouldRemoveBookFromDB_WhenDeleteIsSuccessfully() {
        BookDTO bookDTO = generateBookDTO();
        Book book = bookMapper.toEntity(bookDTO);
        book = bookRepository.save(book);

        bookService.delete(book.getId());

        assertFalse(bookRepository.existsById(book.getId()));
    }

}
