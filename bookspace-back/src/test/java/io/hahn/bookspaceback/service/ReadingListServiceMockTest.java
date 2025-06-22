package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.BookBarebonesDTO;
import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingListServiceMockTest {

    @Mock
    private ReadingListRepository readingListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReadingListMapper readingListMapper;

    @Mock
    private ReadingListRequestMapper readingListRequestMapper;

    @InjectMocks
    private ReadingListService readingListService;

    private ReadingList mockReadingListEntity;
    private ReadingListDTO readingListDTO;
    private ReadingListRequestDTO readingListRequestDTO;
    private User mockUserEntity;
    private Book mockBookEntity;
    private BookBarebonesDTO bookBarebonesDTO;

    @BeforeEach
    void setUp() {
        readingListRequestDTO = new ReadingListRequestDTO();
        readingListRequestDTO.setUserName("Test UUID");
        readingListRequestDTO.setBookID(1L);
        readingListRequestDTO.setStatus(Status.READING);
        readingListRequestDTO.setRating(4);

        bookBarebonesDTO = new BookBarebonesDTO();
        bookBarebonesDTO.setId(1L);
        bookBarebonesDTO.setTitle("Test Book");
        bookBarebonesDTO.setCoverUrl("Test Url");

        readingListDTO = new ReadingListDTO();
        readingListDTO.setId(1L);
        readingListDTO.setUserName("Test User");
        readingListDTO.setBook(bookBarebonesDTO);
        readingListDTO.setAddedAt(LocalDateTime.now());
        readingListDTO.setStartedAt(LocalDateTime.now());
        readingListDTO.setCompletedAt(null);
        readingListDTO.setStatus(Status.READING);
        readingListDTO.setRating(4);

        mockUserEntity = new User();
        ReflectionTestUtils.setField(mockUserEntity, "id", "Test UUID");
        ReflectionTestUtils.setField(mockUserEntity, "userName", "Test User");

        mockBookEntity = new Book();
        ReflectionTestUtils.setField(mockBookEntity, "id", 1L);
        ReflectionTestUtils.setField(mockBookEntity, "title", "Test Book");
        ReflectionTestUtils.setField(mockBookEntity, "coverUrl", "Test Url");

        mockReadingListEntity = new ReadingList();
        ReflectionTestUtils.setField(mockReadingListEntity, "id", 1L);
        ReflectionTestUtils.setField(mockReadingListEntity, "user", mockUserEntity);
        ReflectionTestUtils.setField(mockReadingListEntity, "book", mockBookEntity);
        ReflectionTestUtils.setField(mockReadingListEntity, "addedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(mockReadingListEntity, "startedAt", LocalDateTime.now());
        ReflectionTestUtils.setField(mockReadingListEntity, "completedAt", null);
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.READING);
        ReflectionTestUtils.setField(mockReadingListEntity, "rating", 4);
    }

    @Test
    void create_ShouldReturnReadingListDTO_WhenReadingListIsCreatedSuccessfully() {
        when(userRepository.findByUserName(readingListRequestDTO.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(bookRepository.findById(readingListRequestDTO.getBookID())).thenReturn(Optional.of(mockBookEntity));
        when(readingListRequestMapper.toEntity(readingListRequestDTO)).thenReturn(mockReadingListEntity);
        when(readingListRepository.save(mockReadingListEntity)).thenReturn(mockReadingListEntity);
        when(readingListMapper.toDTO(mockReadingListEntity)).thenReturn(readingListDTO);

        ReadingListDTO result = readingListService.create(readingListRequestDTO);

        assertNotNull(result);
        assertEquals(readingListDTO.getId(), result.getId());
        assertEquals(readingListDTO.getUserName(), result.getUserName());
        assertEquals(readingListDTO.getBook().getId(), result.getBook().getId());
        assertEquals(readingListDTO.getStatus(), result.getStatus());
        assertEquals(readingListDTO.getRating(), result.getRating());
    }

    @Test
    void create_ShouldThrowCustomException_WhenUserNotFound() {
        when(userRepository.findByUserName(readingListRequestDTO.getUserName())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.create(readingListRequestDTO));

        assertEquals("User with username " + readingListRequestDTO.getUserName() + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void create_ShouldThrowCustomException_WhenBookNotFound() {
        when(userRepository.findByUserName(readingListRequestDTO.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(bookRepository.findById(readingListRequestDTO.getBookID())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.create(readingListRequestDTO));

        assertEquals("Book with id " + readingListRequestDTO.getBookID() + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void create_ShouldThrowCustomException_WhenExceptionOccurs() {
        when(userRepository.findByUserName(readingListRequestDTO.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(bookRepository.findById(readingListRequestDTO.getBookID())).thenReturn(Optional.of(mockBookEntity));
        when(readingListRequestMapper.toEntity(readingListRequestDTO)).thenReturn(mockReadingListEntity);
        when(readingListRepository.save(mockReadingListEntity)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.create(readingListRequestDTO));

        assertTrue(exception.getMessage().contains("Failed to create readingList"));
    }

    @Test
    void update_ShouldReturnReadingListDTO_WhenReadingListIsUpdatedSuccessfully() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenReturn(Optional.of(mockReadingListEntity));
        when(readingListRepository.save(mockReadingListEntity)).thenReturn(mockReadingListEntity);
        when(readingListMapper.toDTO(mockReadingListEntity)).thenReturn(readingListDTO);
        doNothing().when(readingListRequestMapper).updateReadingListFromDTO(readingListRequestDTO, mockReadingListEntity);

        ReadingListDTO result = readingListService.update(id, readingListRequestDTO);

        assertNotNull(result);
        assertEquals(readingListDTO.getId(), result.getId());
        assertEquals(readingListDTO.getUserName(), result.getUserName());
        assertEquals(readingListDTO.getBook().getId(), result.getBook().getId());
        assertEquals(readingListDTO.getStatus(), result.getStatus());
        assertEquals(readingListDTO.getRating(), result.getRating());
    }

    @Test
    void update_ShouldThrowCustomException_WhenReadingListNotFound() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.update(id, readingListRequestDTO));

        assertEquals("ReadingList with id " + id + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void update_ShouldThrowCustomException_WhenExceptionOccurs() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.update(id, readingListRequestDTO));

        assertTrue(exception.getMessage().contains("Failed to update readingList"));
    }

    @Test
    void getById_ShouldReturnReadingListDTO_WhenReadingListExists() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenReturn(Optional.of(mockReadingListEntity));
        when(readingListMapper.toDTO(mockReadingListEntity)).thenReturn(readingListDTO);

        ReadingListDTO result = readingListService.getById(id);

        assertNotNull(result);
        assertEquals(readingListDTO.getId(), result.getId());
        assertEquals(readingListDTO.getUserName(), result.getUserName());
        assertEquals(readingListDTO.getBook().getId(), result.getBook().getId());
        assertEquals(readingListDTO.getStatus(), result.getStatus());
        assertEquals(readingListDTO.getRating(), result.getRating());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenReadingListNotFound() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getById(id));

        assertEquals("ReadingList with id " + id + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void getById_ShouldThrowCustomException_WhenExceptionOccurs() {
        Long id = 1L;
        when(readingListRepository.findById(id)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getById(id));

        assertTrue(exception.getMessage().contains("Failed to fetch readingList"));
    }

    @Test
    void getAll_ShouldReturnPageOfReadingListDTO_WhenReadingListsExist() {
        int pageNumber = 0;
        int pageSize = 10;
        when(readingListRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(new PageImpl<>(List.of(mockReadingListEntity)));
        when(readingListMapper.toDTO(mockReadingListEntity)).thenReturn(readingListDTO);

        PageWrapper<ReadingListDTO> result = readingListService.getAll(pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(readingListDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoReadingListsFound() {
        int pageNumber = 0;
        int pageSize = 10;
        when(readingListRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenReturn(new PageImpl<>(List.of()));

        PageWrapper<ReadingListDTO> result = readingListService.getAll(pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageNumberIsNegative() {
        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getAll(-1, 10));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsNegative() {
        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getAll(0, -1));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenPageSizeIsZero() {
        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getAll(0, 0));
        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAll_ShouldThrowCustomException_WhenExceptionOccurs() {
        int pageNumber = 0;
        int pageSize = 10;
        when(readingListRepository.findAll(PageRequest.of(pageNumber, pageSize)))
                .thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.getAll(pageNumber, pageSize));

        assertTrue(exception.getMessage().contains("Failed to fetch all readingLists"));
    }

    @Test
    void delete_ShouldDeleteReadingList_WhenReadingListExists() {
        Long id = 1L;
        doNothing().when(readingListRepository).deleteById(id);

        readingListService.delete(id);

        verify(readingListRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_ShouldThrowCustomException_WhenExceptionOccurs() {
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(readingListRepository).deleteById(id);

        CustomException exception = assertThrows(CustomException.class, () -> readingListService.delete(id));

        assertTrue(exception.getMessage().contains("Failed to delete readingList"));
    }
}