package io.hahn.bookspaceback.service;

import io.hahn.bookspaceback.dto.ReviewDTO;
import io.hahn.bookspaceback.dto.ReviewRequestDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.mapper.ReviewMapper;
import io.hahn.bookspaceback.mapper.ReviewRequestMapper;
import io.hahn.bookspaceback.repository.BookRepository;
import io.hahn.bookspaceback.repository.ReadingListRepository;
import io.hahn.bookspaceback.repository.ReviewRepository;
import io.hahn.bookspaceback.repository.UserRepository;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ReviewServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReadingListRepository readingListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewRequestMapper reviewRequestMapper;

    private ReviewService reviewService;
    private User testUser;
    private Book testBook;
    private ReadingList testReadingList;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        readingListRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        reviewService = new ReviewService(
                reviewRepository,
                readingListRepository,
                reviewRequestMapper,
                reviewMapper
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

        testReadingList = new ReadingList();
        testReadingList.setUser(testUser);
        testReadingList.setBook(testBook);
        testReadingList.setStatus(Status.COMPLETED);
        testReadingList.setRating(5);
        testReadingList = readingListRepository.save(testReadingList);
    }

    private ReviewRequestDTO generateReviewRequestDTO() {
        ReviewRequestDTO dto = new ReviewRequestDTO();
        dto.setReadingListId(testReadingList.getId());
        dto.setComment("Great book, highly recommended!");
        return dto;
    }

    @Test
    void create_ShouldReturnReviewDTO_WhenReviewIsCreatedSuccessfully() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals(testBook.getTitle(), result.getBook().getTitle());
        assertEquals(testBook.getCoverUrl(), result.getBook().getCoverUrl());
        assertEquals("Great book, highly recommended!", result.getComment());
        assertEquals(5, result.getRating());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReviewAlreadyExists() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        reviewService.create(requestDTO);

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(requestDTO));

        assertTrue(exception.getMessage().contains("Review already exists for ReadingList id"));
        assertTrue(exception.getMessage().contains(testReadingList.getId().toString()));
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListNotFound() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        requestDTO.setReadingListId(999L);

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(requestDTO));

        assertTrue(exception.getMessage().contains("ReadingList with id 999 not found"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListStatusIsPlanToRead() {
        testReadingList.setStatus(Status.PLAN_TO_READ);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(requestDTO));

        assertTrue(exception.getMessage().contains("Cannot add review for ReadingList with status PLAN_TO_READ"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void create_ShouldThrowCustomException_WhenReadingListStatusIsOnHold() {
        testReadingList.setStatus(Status.ON_HOLD);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        CustomException exception = assertThrows(CustomException.class, () -> reviewService.create(requestDTO));

        assertTrue(exception.getMessage().contains("Cannot add review for ReadingList with status ON_HOLD"));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void create_ShouldAllowReview_WhenReadingListStatusIsReading() {
        testReadingList.setStatus(Status.READING);
        testReadingList.setRating(4);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals("Great book, highly recommended!", result.getComment());
        assertEquals(4, result.getRating());
    }

    @Test
    void create_ShouldAllowReview_WhenReadingListStatusIsCompleted() {
        testReadingList.setStatus(Status.COMPLETED);
        testReadingList.setRating(5);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals("Great book, highly recommended!", result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void create_ShouldCreateReview_WithNullComment() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        requestDTO.setComment(null);

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertNull(result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void create_ShouldCreateReview_WithEmptyComment() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        requestDTO.setComment("");

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals("", result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void create_ShouldCreateReview_WithLongComment() {
        String longComment = "This is a very long comment that exceeds normal length. ".repeat(20);
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        requestDTO.setComment(longComment);

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testBook.getId(), result.getBook().getId());
        assertEquals(longComment, result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void create_ShouldInheritRatingFromReadingList_WhenReadingListHasRating() {
        testReadingList.setRating(3);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertEquals(3, result.getRating());
    }

    @Test
    void create_ShouldHandleNullRating_WhenReadingListHasNoRating() {
        testReadingList.setRating(null);
        readingListRepository.save(testReadingList);

        ReviewRequestDTO requestDTO = generateReviewRequestDTO();

        ReviewDTO result = reviewService.create(requestDTO);

        assertNotNull(result);
        assertNull(result.getRating());
    }

    @Test
    void delete_ShouldRemoveReviewFromDB_WhenDeleteIsSuccessful() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        ReviewDTO savedReview = reviewService.create(requestDTO);

        reviewService.delete(savedReview.getId());

        assertFalse(reviewRepository.existsById(savedReview.getId()));
    }

    @Test
    void delete_ShouldNotThrowException_WhenReviewDoesNotExist() {
        Long nonExistentId = 999L;

        assertDoesNotThrow(() -> reviewService.delete(nonExistentId));
    }

    @Test
    void delete_ShouldAllowCreatingNewReview_AfterDeletingExistingOne() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        ReviewDTO savedReview = reviewService.create(requestDTO);

        reviewService.delete(savedReview.getId());

        ReviewDTO newReview = reviewService.create(requestDTO);

        assertNotNull(newReview);
        assertNotEquals(savedReview.getId(), newReview.getId());
        assertEquals(savedReview.getComment(), newReview.getComment());
        assertEquals(savedReview.getUsername(), newReview.getUsername());
        assertEquals(savedReview.getBook().getId(), newReview.getBook().getId());
    }

    @Test
    void create_ShouldWork_WithMultipleReadingListsForSameUser() {
        Book secondBook = new Book();
        secondBook.setTitle("Second Book");
        secondBook.setAuthor("Second Author");
        secondBook.setCoverUrl("Second Url");
        secondBook = bookRepository.save(secondBook);

        ReadingList secondReadingList = new ReadingList();
        secondReadingList.setUser(testUser);
        secondReadingList.setBook(secondBook);
        secondReadingList.setStatus(Status.COMPLETED);
        secondReadingList.setRating(4);
        secondReadingList = readingListRepository.save(secondReadingList);

        ReviewRequestDTO firstRequestDTO = generateReviewRequestDTO();
        ReviewRequestDTO secondRequestDTO = new ReviewRequestDTO();
        secondRequestDTO.setReadingListId(secondReadingList.getId());
        secondRequestDTO.setComment("Another great book!");

        ReviewDTO firstReview = reviewService.create(firstRequestDTO);
        ReviewDTO secondReview = reviewService.create(secondRequestDTO);

        assertNotNull(firstReview);
        assertNotNull(secondReview);
        assertNotEquals(firstReview.getId(), secondReview.getId());
        assertEquals(testUser.getUsername(), firstReview.getUsername());
        assertEquals(testUser.getUsername(), secondReview.getUsername());
        assertEquals(testBook.getId(), firstReview.getBook().getId());
        assertEquals(secondBook.getId(), secondReview.getBook().getId());
    }

    @Test
    void create_ShouldWork_WithMultipleUsersForSameBook() {
        User secondUser = new User();
        secondUser.setUsername("seconduser");
        secondUser.setEmail("second@example.com");
        secondUser.setPassword("password456");
        secondUser = userRepository.save(secondUser);

        ReadingList secondUserReadingList = new ReadingList();
        secondUserReadingList.setUser(secondUser);
        secondUserReadingList.setBook(testBook);
        secondUserReadingList.setStatus(Status.COMPLETED);
        secondUserReadingList.setRating(3);
        secondUserReadingList = readingListRepository.save(secondUserReadingList);

        ReviewRequestDTO firstRequestDTO = generateReviewRequestDTO();
        ReviewRequestDTO secondRequestDTO = new ReviewRequestDTO();
        secondRequestDTO.setReadingListId(secondUserReadingList.getId());
        secondRequestDTO.setComment("Different perspective on the same book!");

        ReviewDTO firstReview = reviewService.create(firstRequestDTO);
        ReviewDTO secondReview = reviewService.create(secondRequestDTO);

        assertNotNull(firstReview);
        assertNotNull(secondReview);
        assertNotEquals(firstReview.getId(), secondReview.getId());
        assertEquals(testUser.getUsername(), firstReview.getUsername());
        assertEquals(secondUser.getUsername(), secondReview.getUsername());
        assertEquals(testBook.getId(), firstReview.getBook().getId());
        assertEquals(testBook.getId(), secondReview.getBook().getId());
        assertEquals(5, firstReview.getRating());
        assertEquals(3, secondReview.getRating());
    }

    // Add these test methods to your existing ReviewServiceTest class

    @Test
    void getAllByBookId_ShouldReturnPagedReviews_WhenValidParametersProvided() {
        // Create multiple reviews for the same book
        ReviewRequestDTO requestDTO1 = generateReviewRequestDTO();
        ReviewDTO review1 = reviewService.create(requestDTO1);

        // Create second user and reading list for same book
        User secondUser = new User();
        secondUser.setUsername("seconduser");
        secondUser.setEmail("second@example.com");
        secondUser.setPassword("password456");
        secondUser = userRepository.save(secondUser);

        ReadingList secondUserReadingList = new ReadingList();
        secondUserReadingList.setUser(secondUser);
        secondUserReadingList.setBook(testBook);
        secondUserReadingList.setStatus(Status.COMPLETED);
        secondUserReadingList.setRating(4);
        secondUserReadingList = readingListRepository.save(secondUserReadingList);

        ReviewRequestDTO requestDTO2 = new ReviewRequestDTO();
        requestDTO2.setReadingListId(secondUserReadingList.getId());
        requestDTO2.setComment("Second review for the same book!");
        ReviewDTO review2 = reviewService.create(requestDTO2);

        PageWrapper<ReviewDTO> result = reviewService.getAllByBookId(testBook.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().anyMatch(r -> r.getId().equals(review1.getId())));
        assertTrue(result.getContent().stream().anyMatch(r -> r.getId().equals(review2.getId())));
        assertTrue(result.getContent().stream().allMatch(r -> r.getBook().getId().equals(testBook.getId())));
    }

    @Test
    void getAllByBookId_ShouldReturnEmptyPage_WhenNoReviewsExistForBook() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setCoverUrl("New Url");
        newBook = bookRepository.save(newBook);

        PageWrapper<ReviewDTO> result = reviewService.getAllByBookId(newBook.getId(), 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByBookId_ShouldHandlePagination_WhenMultipleReviewsExist() {
        // Create multiple users and reviews for the same book
        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword("password" + i);
            user = userRepository.save(user);

            ReadingList readingList = new ReadingList();
            readingList.setUser(user);
            readingList.setBook(testBook);
            readingList.setStatus(Status.COMPLETED);
            readingList.setRating(i + 1);
            readingList = readingListRepository.save(readingList);

            ReviewRequestDTO requestDTO = new ReviewRequestDTO();
            requestDTO.setReadingListId(readingList.getId());
            requestDTO.setComment("Review " + i);
            reviewService.create(requestDTO);
        }

        // Test first page with page size 2
        PageWrapper<ReviewDTO> firstPage = reviewService.getAllByBookId(testBook.getId(), 0, 2);
        assertNotNull(firstPage);
        assertEquals(2, firstPage.getContent().size());
        assertEquals(5, firstPage.getTotalElements());

        // Test second page
        PageWrapper<ReviewDTO> secondPage = reviewService.getAllByBookId(testBook.getId(), 1, 2);
        assertNotNull(secondPage);
        assertEquals(2, secondPage.getContent().size());

        // Test third page
        PageWrapper<ReviewDTO> thirdPage = reviewService.getAllByBookId(testBook.getId(), 2, 2);
        assertNotNull(thirdPage);
        assertEquals(1, thirdPage.getContent().size());
    }

    @Test
    void getAllByBookId_ShouldThrowCustomException_WhenPageNumberIsNegative() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByBookId(testBook.getId(), -1, 10));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByBookId_ShouldThrowCustomException_WhenPageSizeIsZero() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByBookId(testBook.getId(), 0, 0));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByBookId_ShouldThrowCustomException_WhenPageSizeIsNegative() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByBookId(testBook.getId(), 0, -5));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByBookId_ShouldHandleNonExistentBookId() {
        Long nonExistentBookId = 999L;

        PageWrapper<ReviewDTO> result = reviewService.getAllByBookId(nonExistentBookId, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByUsername_ShouldReturnPagedReviews_WhenValidParametersProvided() {
        // Create multiple books and reviews for the same user
        ReviewRequestDTO requestDTO1 = generateReviewRequestDTO();
        ReviewDTO review1 = reviewService.create(requestDTO1);

        // Create second book and reading list for same user
        Book secondBook = new Book();
        secondBook.setTitle("Second Book");
        secondBook.setAuthor("Second Author");
        secondBook.setCoverUrl("Second Url");
        secondBook = bookRepository.save(secondBook);

        ReadingList secondReadingList = new ReadingList();
        secondReadingList.setUser(testUser);
        secondReadingList.setBook(secondBook);
        secondReadingList.setStatus(Status.COMPLETED);
        secondReadingList.setRating(3);
        secondReadingList = readingListRepository.save(secondReadingList);

        ReviewRequestDTO requestDTO2 = new ReviewRequestDTO();
        requestDTO2.setReadingListId(secondReadingList.getId());
        requestDTO2.setComment("Second review by same user!");
        ReviewDTO review2 = reviewService.create(requestDTO2);

        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername(testUser.getUsername(), 0, 10);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().anyMatch(r -> r.getId().equals(review1.getId())));
        assertTrue(result.getContent().stream().anyMatch(r -> r.getId().equals(review2.getId())));
        assertTrue(result.getContent().stream().allMatch(r -> r.getUsername().equals(testUser.getUsername())));
    }

    @Test
    void getAllByUsername_ShouldReturnEmptyPage_WhenNoReviewsExistForUser() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setPassword("newpassword");
        newUser = userRepository.save(newUser);

        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername(newUser.getUsername(), 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByUsername_ShouldHandlePagination_WhenMultipleReviewsExist() {
        // Create multiple books and reviews for the same user
        for (int i = 0; i < 5; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor("Author " + i);
            book.setCoverUrl("Url " + i);
            book = bookRepository.save(book);

            ReadingList readingList = new ReadingList();
            readingList.setUser(testUser);
            readingList.setBook(book);
            readingList.setStatus(Status.COMPLETED);
            readingList.setRating(i + 1);
            readingList = readingListRepository.save(readingList);

            ReviewRequestDTO requestDTO = new ReviewRequestDTO();
            requestDTO.setReadingListId(readingList.getId());
            requestDTO.setComment("Review for book " + i);
            reviewService.create(requestDTO);
        }

        // Test first page with page size 2
        PageWrapper<ReviewDTO> firstPage = reviewService.getAllByUsername(testUser.getUsername(), 0, 2);
        assertNotNull(firstPage);
        assertEquals(2, firstPage.getContent().size());
        assertEquals(5, firstPage.getTotalElements());

        // Test second page
        PageWrapper<ReviewDTO> secondPage = reviewService.getAllByUsername(testUser.getUsername(), 1, 2);
        assertNotNull(secondPage);
        assertEquals(2, secondPage.getContent().size());

        // Test third page
        PageWrapper<ReviewDTO> thirdPage = reviewService.getAllByUsername(testUser.getUsername(), 2, 2);
        assertNotNull(thirdPage);
        assertEquals(1, thirdPage.getContent().size());
    }

    @Test
    void getAllByUsername_ShouldThrowCustomException_WhenPageNumberIsNegative() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByUsername(testUser.getUsername(), -1, 10));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByUsername_ShouldThrowCustomException_WhenPageSizeIsZero() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByUsername(testUser.getUsername(), 0, 0));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByUsername_ShouldThrowCustomException_WhenPageSizeIsNegative() {
        CustomException exception = assertThrows(CustomException.class,
                () -> reviewService.getAllByUsername(testUser.getUsername(), 0, -5));

        assertEquals("Invalid pagination parameters", exception.getMessage());
    }

    @Test
    void getAllByUsername_ShouldHandleNonExistentUsername() {
        String nonExistentUsername = "nonexistentuser";

        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername(nonExistentUsername, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByUsername_ShouldHandleNullUsername() {
        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername(null, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByUsername_ShouldHandleEmptyUsername() {
        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername("", 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllByBookId_ShouldReturnCorrectReviewData_WhenReviewsExist() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        ReviewDTO createdReview = reviewService.create(requestDTO);

        PageWrapper<ReviewDTO> result = reviewService.getAllByBookId(testBook.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        ReviewDTO returnedReview = result.getContent().get(0);
        assertEquals(createdReview.getId(), returnedReview.getId());
        assertEquals(createdReview.getUsername(), returnedReview.getUsername());
        assertEquals(createdReview.getComment(), returnedReview.getComment());
        assertEquals(createdReview.getRating(), returnedReview.getRating());
        assertEquals(createdReview.getBook().getId(), returnedReview.getBook().getId());
        assertEquals(createdReview.getBook().getTitle(), returnedReview.getBook().getTitle());
    }

    @Test
    void getAllByUsername_ShouldReturnCorrectReviewData_WhenReviewsExist() {
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        ReviewDTO createdReview = reviewService.create(requestDTO);

        PageWrapper<ReviewDTO> result = reviewService.getAllByUsername(testUser.getUsername(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        ReviewDTO returnedReview = result.getContent().get(0);
        assertEquals(createdReview.getId(), returnedReview.getId());
        assertEquals(createdReview.getUsername(), returnedReview.getUsername());
        assertEquals(createdReview.getComment(), returnedReview.getComment());
        assertEquals(createdReview.getRating(), returnedReview.getRating());
        assertEquals(createdReview.getBook().getId(), returnedReview.getBook().getId());
        assertEquals(createdReview.getBook().getTitle(), returnedReview.getBook().getTitle());
    }

    @Test
    void delete_ShouldCascadeDeleteReviews_WhenUserIsDeleted() {
        // Create multiple reviews for the test user
        ReviewRequestDTO requestDTO1 = generateReviewRequestDTO();
        ReviewDTO review1 = reviewService.create(requestDTO1);

        // Create second book and reading list for same user
        Book secondBook = new Book();
        secondBook.setTitle("Second Book");
        secondBook.setAuthor("Second Author");
        secondBook.setCoverUrl("Second Url");
        secondBook = bookRepository.save(secondBook);

        ReadingList secondReadingList = new ReadingList();
        secondReadingList.setUser(testUser);
        secondReadingList.setBook(secondBook);
        secondReadingList.setStatus(Status.COMPLETED);
        secondReadingList.setRating(4);
        secondReadingList = readingListRepository.save(secondReadingList);

        ReviewRequestDTO requestDTO2 = new ReviewRequestDTO();
        requestDTO2.setReadingListId(secondReadingList.getId());
        requestDTO2.setComment("Second review by same user!");
        ReviewDTO review2 = reviewService.create(requestDTO2);

        // Create another user with a review to ensure other reviews are not affected
        User otherUser = new User();
        otherUser.setUsername("otheruser");
        otherUser.setEmail("other@example.com");
        otherUser.setPassword("otherpassword");
        otherUser = userRepository.save(otherUser);

        ReadingList otherUserReadingList = new ReadingList();
        otherUserReadingList.setUser(otherUser);
        otherUserReadingList.setBook(testBook);
        otherUserReadingList.setStatus(Status.COMPLETED);
        otherUserReadingList.setRating(3);
        otherUserReadingList = readingListRepository.save(otherUserReadingList);

        ReviewRequestDTO otherRequestDTO = new ReviewRequestDTO();
        otherRequestDTO.setReadingListId(otherUserReadingList.getId());
        otherRequestDTO.setComment("Review by other user!");
        ReviewDTO otherUserReview = reviewService.create(otherRequestDTO);

        // Verify reviews exist before deletion
        assertEquals(3, reviewRepository.count());
        assertTrue(reviewRepository.existsById(review1.getId()));
        assertTrue(reviewRepository.existsById(review2.getId()));
        assertTrue(reviewRepository.existsById(otherUserReview.getId()));

        // Delete the test user
        userRepository.delete(testUser);

        // Verify that reviews associated with the deleted user are also deleted
        assertEquals(1, reviewRepository.count());
        assertFalse(reviewRepository.existsById(review1.getId()));
        assertFalse(reviewRepository.existsById(review2.getId()));
        // Other user's review should still exist
        assertTrue(reviewRepository.existsById(otherUserReview.getId()));

        // Verify the remaining review belongs to the other user
        PageWrapper<ReviewDTO> remainingReviews = reviewService.getAllByUsername(otherUser.getUsername(), 0, 10);
        assertEquals(1, remainingReviews.getContent().size());
        assertEquals(otherUserReview.getId(), remainingReviews.getContent().get(0).getId());
    }

    @Test
    void delete_ShouldCascadeDeleteReviews_WhenUserIsDeletedByRepository() {
        // Create a review for the test user
        ReviewRequestDTO requestDTO = generateReviewRequestDTO();
        ReviewDTO review = reviewService.create(requestDTO);

        // Verify review exists
        assertTrue(reviewRepository.existsById(review.getId()));
        assertEquals(1, reviewRepository.count());

        // Delete user by ID using repository
        userRepository.deleteById(testUser.getId());

        // Verify review is also deleted
        assertFalse(reviewRepository.existsById(review.getId()));
        assertEquals(0, reviewRepository.count());
    }

    @Test
    void delete_ShouldHandleMultipleUsersWithReviews_WhenOneUserIsDeleted() {
        // Create reviews for multiple users
        ReviewRequestDTO requestDTO1 = generateReviewRequestDTO();
        ReviewDTO testUserReview = reviewService.create(requestDTO1);

        // Create second user
        User secondUser = new User();
        secondUser.setUsername("seconduser");
        secondUser.setEmail("second@example.com");
        secondUser.setPassword("secondpassword");
        secondUser = userRepository.save(secondUser);

        ReadingList secondUserReadingList = new ReadingList();
        secondUserReadingList.setUser(secondUser);
        secondUserReadingList.setBook(testBook);
        secondUserReadingList.setStatus(Status.COMPLETED);
        secondUserReadingList.setRating(4);
        secondUserReadingList = readingListRepository.save(secondUserReadingList);

        ReviewRequestDTO requestDTO2 = new ReviewRequestDTO();
        requestDTO2.setReadingListId(secondUserReadingList.getId());
        requestDTO2.setComment("Review by second user!");
        ReviewDTO secondUserReview = reviewService.create(requestDTO2);

        // Create third user
        User thirdUser = new User();
        thirdUser.setUsername("thirduser");
        thirdUser.setEmail("third@example.com");
        thirdUser.setPassword("thirdpassword");
        thirdUser = userRepository.save(thirdUser);

        ReadingList thirdUserReadingList = new ReadingList();
        thirdUserReadingList.setUser(thirdUser);
        thirdUserReadingList.setBook(testBook);
        thirdUserReadingList.setStatus(Status.COMPLETED);
        thirdUserReadingList.setRating(2);
        thirdUserReadingList = readingListRepository.save(thirdUserReadingList);

        ReviewRequestDTO requestDTO3 = new ReviewRequestDTO();
        requestDTO3.setReadingListId(thirdUserReadingList.getId());
        requestDTO3.setComment("Review by third user!");
        ReviewDTO thirdUserReview = reviewService.create(requestDTO3);

        // Verify all reviews exist
        assertEquals(3, reviewRepository.count());

        // Delete only the second user
        userRepository.delete(secondUser);

        // Verify only the second user's review is deleted
        assertEquals(2, reviewRepository.count());
        assertTrue(reviewRepository.existsById(testUserReview.getId()));
        assertFalse(reviewRepository.existsById(secondUserReview.getId()));
        assertTrue(reviewRepository.existsById(thirdUserReview.getId()));

        // Verify reviews can still be retrieved for remaining users
        PageWrapper<ReviewDTO> testUserReviews = reviewService.getAllByUsername(testUser.getUsername(), 0, 10);
        assertEquals(1, testUserReviews.getContent().size());

        PageWrapper<ReviewDTO> thirdUserReviews = reviewService.getAllByUsername(thirdUser.getUsername(), 0, 10);
        assertEquals(1, thirdUserReviews.getContent().size());

        PageWrapper<ReviewDTO> secondUserReviews = reviewService.getAllByUsername(secondUser.getUsername(), 0, 10);
        assertEquals(0, secondUserReviews.getContent().size());
    }
}