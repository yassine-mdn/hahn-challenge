package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.BookBarebonesDTO;
import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.dto.ReviewRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.Review;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.ReviewMapper;
import io.hahn.bookspaceback.mapper.ReviewRequestMapper;
import io.hahn.bookspaceback.repository.ReadingListRepository;
import io.hahn.bookspaceback.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReviewServiceMockTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReadingListRepository readingListRepository;

    @Mock
    private ReviewRequestMapper reviewRequestMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    private Review mockReviewEntity;
    private ReviewDTO reviewDTO;
    private ReviewRequestDTO reviewRequestDTO;
    private ReadingList mockReadingListEntity;
    private User mockUserEntity;
    private Book mockBookEntity;
    private BookBarebonesDTO bookBarebonesDTO;

    @BeforeEach
    void setUp() {
        reviewRequestDTO = new ReviewRequestDTO();
        reviewRequestDTO.setReadingListId(1L);
        reviewRequestDTO.setComment("Great book, highly recommended!");

        bookBarebonesDTO = new BookBarebonesDTO();
        bookBarebonesDTO.setId(1L);
        bookBarebonesDTO.setTitle("Test Book");
        bookBarebonesDTO.setCoverUrl("Test Url");

        reviewDTO = new ReviewDTO();
        reviewDTO.setId(1L);
        reviewDTO.setBook(bookBarebonesDTO);
        reviewDTO.setUsername("testUser");
        reviewDTO.setComment("Great book, highly recommended!");
        reviewDTO.setRating(5);
        reviewDTO.setCreatedAt(LocalDateTime.now());

        mockUserEntity = new User();
        ReflectionTestUtils.setField(mockUserEntity, "id", "test-uuid");
        ReflectionTestUtils.setField(mockUserEntity, "userName", "testUser");

        mockBookEntity = new Book();
        ReflectionTestUtils.setField(mockBookEntity, "id", 1L);
        ReflectionTestUtils.setField(mockBookEntity, "title", "Test Book");
        ReflectionTestUtils.setField(mockBookEntity, "coverUrl", "Test Url");

        mockReadingListEntity = new ReadingList();
        ReflectionTestUtils.setField(mockReadingListEntity, "id", 1L);
        ReflectionTestUtils.setField(mockReadingListEntity, "user", mockUserEntity);
        ReflectionTestUtils.setField(mockReadingListEntity, "book", mockBookEntity);
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.COMPLETED);
        ReflectionTestUtils.setField(mockReadingListEntity, "rating", 5);

        mockReviewEntity = new Review();
        ReflectionTestUtils.setField(mockReviewEntity, "id", 1L);
        ReflectionTestUtils.setField(mockReviewEntity, "readingList", mockReadingListEntity);
        ReflectionTestUtils.setField(mockReviewEntity, "comment", "Great book, highly recommended!");
        ReflectionTestUtils.setField(mockReviewEntity, "createdAt", LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnReviewDTO_WhenReviewIsCreatedSuccessfully() {
        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));
        when(reviewRequestMapper.toEntity(reviewRequestDTO)).thenReturn(mockReviewEntity);
        when(reviewRepository.save(mockReviewEntity)).thenReturn(mockReviewEntity);
        when(reviewMapper.toDTO(mockReviewEntity)).thenReturn(reviewDTO);

        ReviewDTO result = reviewService.create(reviewRequestDTO);

        assertNotNull(result);
        assertEquals(reviewDTO.getId(), result.getId());
        assertEquals(reviewDTO.getComment(), result.getComment());
        assertEquals(reviewDTO.getUsername(), result.getUsername());
        assertEquals(reviewDTO.getBook().getId(), result.getBook().getId());
        assertEquals(reviewDTO.getRating(), result.getRating());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper).toEntity(reviewRequestDTO);
        verify(reviewRepository).save(mockReviewEntity);
        verify(reviewMapper).toDTO(mockReviewEntity);
    }

    @Test
    void create_ShouldThrowCustomException_WhenReviewAlreadyExists() {
        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertEquals("Review already exists for ReadingList id " + reviewRequestDTO.getReadingListId(), exception.getMessage());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository, never()).findById(any());
        verify(reviewRequestMapper, never()).toEntity(any());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListNotFound() {
        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertEquals("ReadingList with id " + reviewRequestDTO.getReadingListId() + " not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper, never()).toEntity(any());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListStatusIsPlanToRead() {
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.PLAN_TO_READ);

        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertEquals("Cannot add review for ReadingList with status " + Status.PLAN_TO_READ, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper, never()).toEntity(any());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListStatusIsOnHold() {
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.ON_HOLD);

        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertEquals("Cannot add review for ReadingList with status " + Status.ON_HOLD, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper, never()).toEntity(any());
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldAllowReview_WhenReadingListStatusIsReading() {
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.READING);

        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));
        when(reviewRequestMapper.toEntity(reviewRequestDTO)).thenReturn(mockReviewEntity);
        when(reviewRepository.save(mockReviewEntity)).thenReturn(mockReviewEntity);
        when(reviewMapper.toDTO(mockReviewEntity)).thenReturn(reviewDTO);

        ReviewDTO result = reviewService.create(reviewRequestDTO);

        assertNotNull(result);
        assertEquals(reviewDTO.getId(), result.getId());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper).toEntity(reviewRequestDTO);
        verify(reviewRepository).save(mockReviewEntity);
        verify(reviewMapper).toDTO(mockReviewEntity);
    }

    @Test
    void create_ShouldAllowReview_WhenReadingListStatusIsCompleted() {
        ReflectionTestUtils.setField(mockReadingListEntity, "status", Status.COMPLETED);

        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));
        when(reviewRequestMapper.toEntity(reviewRequestDTO)).thenReturn(mockReviewEntity);
        when(reviewRepository.save(mockReviewEntity)).thenReturn(mockReviewEntity);
        when(reviewMapper.toDTO(mockReviewEntity)).thenReturn(reviewDTO);

        ReviewDTO result = reviewService.create(reviewRequestDTO);

        assertNotNull(result);
        assertEquals(reviewDTO.getId(), result.getId());

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper).toEntity(reviewRequestDTO);
        verify(reviewRepository).save(mockReviewEntity);
        verify(reviewMapper).toDTO(mockReviewEntity);
    }

    @Test
    void create_ShouldThrowCustomException_WhenRepositorySaveThrowsException() {
        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));
        when(reviewRequestMapper.toEntity(reviewRequestDTO)).thenReturn(mockReviewEntity);
        when(reviewRepository.save(mockReviewEntity)).thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertTrue(exception.getMessage().contains("Failed to add review"));

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper).toEntity(reviewRequestDTO);
        verify(reviewRepository).save(mockReviewEntity);
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void create_ShouldThrowCustomException_WhenMapperThrowsException() {
        when(reviewRepository.existsByReadingList_Id(reviewRequestDTO.getReadingListId())).thenReturn(false);
        when(readingListRepository.findById(reviewRequestDTO.getReadingListId())).thenReturn(Optional.of(mockReadingListEntity));
        when(reviewRequestMapper.toEntity(reviewRequestDTO)).thenThrow(new RuntimeException("Mapping error"));

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(reviewRequestDTO));

        assertTrue(exception.getMessage().contains("Failed to add review"));

        verify(reviewRepository).existsByReadingList_Id(reviewRequestDTO.getReadingListId());
        verify(readingListRepository).findById(reviewRequestDTO.getReadingListId());
        verify(reviewRequestMapper).toEntity(reviewRequestDTO);
        verify(reviewRepository, never()).save(any());
        verify(reviewMapper, never()).toDTO(any());
    }

    @Test
    void delete_ShouldDeleteReview_WhenReviewExists() {
        Long id = 1L;
        doNothing().when(reviewRepository).deleteById(id);

        reviewService.delete(id);

        verify(reviewRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_ShouldThrowCustomException_WhenRepositoryThrowsException() {
        Long id = 1L;
        doThrow(new RuntimeException("Database error")).when(reviewRepository).deleteById(id);

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.delete(id));

        assertTrue(exception.getMessage().contains("Failed to delete review with id " + id));

        verify(reviewRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_ShouldThrowCustomException_WhenDataIntegrityViolationOccurs() {
        Long id = 1L;
        doThrow(new org.springframework.dao.DataIntegrityViolationException("Constraint violation"))
                .when(reviewRepository).deleteById(id);

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.delete(id));

        assertTrue(exception.getMessage().contains("Failed to delete review with id " + id));

        verify(reviewRepository, times(1)).deleteById(id);
    }
}