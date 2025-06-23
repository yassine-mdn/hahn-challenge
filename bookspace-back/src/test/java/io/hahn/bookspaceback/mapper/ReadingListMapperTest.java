package io.hahn.bookspaceback.mapper;

import io.hahn.bookspaceback.dto.BookBarebonesDTO;
import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.entity.Book;
import io.hahn.bookspaceback.entity.ReadingList;
import io.hahn.bookspaceback.entity.User;
import io.hahn.bookspaceback.entity.enums.Genre;
import io.hahn.bookspaceback.entity.enums.Role;
import io.hahn.bookspaceback.entity.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReadingListMapperTest {

    @Autowired
    private ReadingListMapper readingListMapper;

    private User mockUser;
    private Book mockBook;
    private ReadingList mockReadingList;
    private BookBarebonesDTO mockBookBarebonesDTO;

    @BeforeEach
    void setUp() {

        
        mockUser = new User();
        mockUser.setId("test-uuid");
        mockUser.setUsername("test user");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setRole(Role.USER);

        mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Test Book");
        mockBook.setAuthor("Test Author");
        mockBook.setPublisher("Test Publisher");
        mockBook.setGenres(Arrays.asList(Genre.FICTION, Genre.FANTASY));
        mockBook.setDescription("Test Description");
        mockBook.setCoverUrl("Test Url");
        mockBook.setIsFeatured(true);

        mockReadingList = new ReadingList();
        mockReadingList.setId(1L);
        mockReadingList.setUser(mockUser);
        mockReadingList.setBook(mockBook);
        mockReadingList.setStatus(Status.COMPLETED);
        mockReadingList.setRating(5);

        mockBookBarebonesDTO = new BookBarebonesDTO();
        mockBookBarebonesDTO.setId(2L);
        mockBookBarebonesDTO.setTitle("New Book");
        mockBookBarebonesDTO.setCoverUrl("New Test Url");


    }

    @Test
    void toDTO_ShouldMapEntityToDTO() {
        ReadingListDTO result = readingListMapper.toDTO(mockReadingList);


        assertNotNull(result);
        assertEquals(mockReadingList.getId(), result.getId());
        assertEquals(mockReadingList.getUser().getUsername(), result.getUsername());
        assertNotNull(result.getBook());
        assertEquals(mockReadingList.getBook().getId(), result.getBook().getId());
        assertEquals(mockReadingList.getBook().getTitle(), result.getBook().getTitle());
        assertEquals(mockReadingList.getBook().getCoverUrl(), result.getBook().getCoverUrl());
        assertEquals(mockReadingList.getStatus(), result.getStatus());
        assertEquals(mockReadingList.getRating(), result.getRating());
    }



    @Test
    void updateReadingListFromDto_ShouldUpdateOnlyStatusAndRating() {
        ReadingList readingListToUpdate = new ReadingList();
        readingListToUpdate.setId(1L);
        readingListToUpdate.setUser(mockUser);
        readingListToUpdate.setBook(mockBook);
        readingListToUpdate.setStatus(Status.READING);
        readingListToUpdate.setRating(3);

        ReadingListDTO updateDTO = new ReadingListDTO();
        updateDTO.setId(2L);
        updateDTO.setStatus(Status.COMPLETED);
        updateDTO.setRating(5);
        updateDTO.setUsername("new user");
        updateDTO.setBook(mockBookBarebonesDTO);
        updateDTO.setCompletedAt(LocalDateTime.now());


        readingListMapper.updateReadingListFromDto(updateDTO, readingListToUpdate);

        assertNotEquals(2L, readingListToUpdate.getId());
        assertNotEquals(updateDTO.getUsername(),readingListToUpdate.getUser().getUsername());
        assertNotEquals(updateDTO.getBook().getId(), readingListToUpdate.getBook().getId());
        assertNull(readingListToUpdate.getCompletedAt());
        assertEquals(Status.COMPLETED, readingListToUpdate.getStatus());
        assertEquals(5, readingListToUpdate.getRating());
    }

}
