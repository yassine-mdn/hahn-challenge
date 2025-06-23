package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.ReadingListMapper;
import io.hahn.bookspaceback.mapper.ReadingListRequestMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import io.hahn.bookspaceback.repository.ReadingListRepository;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReadingListServiceTest {

    @Autowired
    private ReadingListRepository readingListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReadingListMapper readingListMapper;

    @Autowired
    private ReadingListRequestMapper readingListRequestMapper;

    private ReadingListService readingListService;
    private User testUser;
    private Book testBook;

    @BeforeEach
    void setUp() {
        readingListRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        readingListService = new ReadingListService(
            readingListRepository, 
            userRepository, 
            bookRepository, 
            readingListMapper, 
            readingListRequestMapper
        );

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser = userRepository.save(testUser);

        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setCoverUrl("Test Url");
        testBook = bookRepository.save(testBook);
    }

    private ReadingListRequestDTO generateReadingListRequestDTO() {
        ReadingListRequestDTO dto = new ReadingListRequestDTO();
        dto.setUsername(testUser.getUsername());
        dto.setBookID(testBook.getId());
        dto.setStatus(Status.READING);
        dto.setRating(4);
        return dto;
    }

    @Test
    void create_ShouldReturnReadingListDTO_WhenReadingListIsCreatedSuccessfully() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();

        ReadingListDTO result = readingListService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals(testBook.getTitle(), result.getBook().getTitle());
        assertEquals(Status.READING, result.getStatus());
        assertEquals(4, result.getRating());
    }

    @Test
    void create_ShouldThrowCustomException_WhenUserNotFound() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        requestDTO.setUsername("non-existent-id");

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.create(requestDTO));

        assertTrue(exception.getMessage().contains("User with username " + requestDTO.getUsername() + " not found"));

    }

    @Test
    void create_ShouldThrowCustomException_WhenBookNotFound() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        requestDTO.setBookID(999L);

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.create(requestDTO));

        assertTrue(exception.getMessage().contains("Book with id"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void update_ShouldReturnReadingListDTO_WhenReadingListIsUpdatedSuccessfully() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        ReadingListDTO savedReadingList = readingListService.create(requestDTO);

        ReadingListRequestDTO updateDTO = new ReadingListRequestDTO();
        updateDTO.setStatus(Status.COMPLETED);
        updateDTO.setRating(5);

        ReadingListDTO result = readingListService.update(savedReadingList.getId(), updateDTO);

        assertNotNull(result);
        assertEquals(savedReadingList.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals(Status.COMPLETED, result.getStatus());
        assertEquals(5, result.getRating());
    }

    @Test
    void update_ShouldThrowCustomException_WhenReadingListNotFound() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();

        CustomException exception = assertThrows(CustomException.class, 
            () -> readingListService.update(999L, requestDTO));

        assertTrue(exception.getMessage().contains("ReadingList with id"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getById_ShouldReturnReadingListDTO_WhenReadingListExists() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        ReadingListDTO savedReadingList = readingListService.create(requestDTO);

        ReadingListDTO result = readingListService.getById(savedReadingList.getId());

        assertNotNull(result);
        assertEquals(savedReadingList.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals(Status.READING, result.getStatus());
        assertEquals(4, result.getRating());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenReadingListNotFound() {
        CustomException exception = assertThrows(CustomException.class, 
            () -> readingListService.getById(999L));

        assertTrue(exception.getMessage().contains("ReadingList with id"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getAll_ShouldReturnPageOfReadingListDTO_WhenReadingListsExist() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        readingListService.create(requestDTO);

        Book secondBook = new Book();
        secondBook.setTitle("Second Book");
        secondBook.setAuthor("Second Author");
        secondBook.setCoverUrl("Second Url");
        secondBook = bookRepository.save(secondBook);

        ReadingListRequestDTO secondRequestDTO = new ReadingListRequestDTO();
        secondRequestDTO.setUsername(testUser.getUsername());
        secondRequestDTO.setBookID(secondBook.getId());
        secondRequestDTO.setStatus(Status.PLAN_TO_READ);
        secondRequestDTO.setRating(null);
        readingListService.create(secondRequestDTO);

        PageWrapper<ReadingListDTO> result = readingListService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getAllByUsername_ShouldReturnPageOfReadingListDTO_WhenReadingListsExist() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        readingListService.create(requestDTO);

        Book secondBook = new Book();
        secondBook.setTitle("Second Book");
        secondBook.setAuthor("Second Author");
        secondBook.setCoverUrl("Second Url");
        secondBook = bookRepository.save(secondBook);

        ReadingListRequestDTO secondRequestDTO = new ReadingListRequestDTO();
        secondRequestDTO.setUsername(testUser.getUsername());
        secondRequestDTO.setBookID(secondBook.getId());
        secondRequestDTO.setStatus(Status.PLAN_TO_READ);
        secondRequestDTO.setRating(null);
        readingListService.create(secondRequestDTO);

        PageWrapper<ReadingListDTO> result = readingListService.getAllByUser(testUser.getUsername(), 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenInvalidPaginationParameters() {
        CustomException exception = assertThrows(CustomException.class, 
            () -> readingListService.getAll(-1, 10));

        assertEquals("Invalid pagination parameters", exception.getMessage());

        exception = assertThrows(CustomException.class, 
            () -> readingListService.getAll(0, 0));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void delete_ShouldRemoveReadingListFromDB_WhenDeleteIsSuccessful() {
        ReadingListRequestDTO requestDTO = generateReadingListRequestDTO();
        ReadingListDTO savedReadingList = readingListService.create(requestDTO);

        readingListService.delete(savedReadingList.getId());

        CustomException exception = assertThrows(CustomException.class, 
            () -> readingListService.getById(savedReadingList.getId()));

        assertTrue(exception.getMessage().contains("ReadingList with id"));
        assertTrue(exception.getMessage().contains("not found"));
    }
}
