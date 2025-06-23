package io.hahn.bookspaceback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hahn.bookspaceback.dto.BookDTO;
import io.hahn.bookspaceback.entity.enums.Genre;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.exception.GlobalExceptionHandler;
import io.hahn.bookspaceback.service.BookService;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private BookDTO mockBookDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mockBookDTO = new BookDTO();
        mockBookDTO.setId(1L);
        mockBookDTO.setTitle("Test Book");
        mockBookDTO.setAuthor("Test Author");
        mockBookDTO.setPublisher("Test Publisher");
        mockBookDTO.setGenres(Arrays.asList(Genre.FICTION, Genre.FANTASY));
        mockBookDTO.setDescription("Test Description");
        mockBookDTO.setCoverUrl("Test Cover URL");
        mockBookDTO.setIsFeatured(true);
    }

    @Test
    void create_ShouldReturnCreatedBookDTO_WhenSuccess() throws Exception {
        when(bookService.create(any(BookDTO.class))).thenReturn(mockBookDTO);

        String jsonRequest = objectMapper.writeValueAsString(mockBookDTO);
        mockMvc.perform(post("/api/v1/books")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.genres[0]").value("FICTION"))
                .andExpect(jsonPath("$.genres[1]").value("FANTASY"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.coverUrl").value("Test Cover URL"))
                .andExpect(jsonPath("$.isFeatured").value(true));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(mockBookDTO);

        when(bookService.create(any(BookDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(post("/api/v1/books")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ShouldReturnBadRequest_InvalidJson() throws Exception {
        String invalidJson = "{invalid json}";

        mockMvc.perform(post("/api/v1/books")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnUpdatedBookDTO_WhenSuccess() throws Exception {
        Long id = 1L;
        String jsonRequest = objectMapper.writeValueAsString(mockBookDTO);

        when(bookService.update(eq(id), any(BookDTO.class))).thenReturn(mockBookDTO);

        mockMvc.perform(put("/api/v1/books/" + id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.genres[0]").value("FICTION"))
                .andExpect(jsonPath("$.genres[1]").value("FANTASY"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.coverUrl").value("Test Cover URL"))
                .andExpect(jsonPath("$.isFeatured").value(true));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;
        String jsonRequest = objectMapper.writeValueAsString(mockBookDTO);

        when(bookService.update(eq(id), any(BookDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(put("/api/v1/books/" + id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnBadRequest_InvalidJson() throws Exception {
        Long id = 1L;
        String invalidJson = "{invalid json}";

        mockMvc.perform(put("/api/v1/books/" + id)
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_ShouldReturnBookDTO_WhenSuccess() throws Exception {
        Long id = 1L;

        when(bookService.getById(id)).thenReturn(mockBookDTO);

        mockMvc.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.genres[0]").value("FICTION"))
                .andExpect(jsonPath("$.genres[1]").value("FANTASY"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.coverUrl").value("Test Cover URL"))
                .andExpect(jsonPath("$.isFeatured").value(true));
    }

    @Test
    void getById_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;

        when(bookService.getById(id)).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/books/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_ShouldReturnPageOfBookDTOs_WhenSuccess() throws Exception {
        PageWrapper<BookDTO> bookPage = new PageWrapper<>(List.of(mockBookDTO));

        when(bookService.getAll(null,0, 10)).thenReturn(bookPage);

        mockMvc.perform(get("/api/v1/books?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Book"))
                .andExpect(jsonPath("$.content[0].author").value("Test Author"))
                .andExpect(jsonPath("$.content[0].publisher").value("Test Publisher"))
                .andExpect(jsonPath("$.content[0].genres[0]").value("FICTION"))
                .andExpect(jsonPath("$.content[0].genres[1]").value("FANTASY"))
                .andExpect(jsonPath("$.content[0].description").value("Test Description"))
                .andExpect(jsonPath("$.content[0].coverUrl").value("Test Cover URL"))
                .andExpect(jsonPath("$.content[0].isFeatured").value(true));
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoBooks() throws Exception {
        PageWrapper<BookDTO> emptyPage = new PageWrapper<>(List.of());

        when(bookService.getAll(null,0, 10)).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/books?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getAll_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        when(bookService.getAll(null,0, 10)).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_ShouldReturnNoContent_WhenSuccess() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v1/books/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;

        doThrow(new CustomException("Error occurred")).when(bookService).delete(id);

        mockMvc.perform(delete("/api/v1/books/" + id))
                .andExpect(status().isBadRequest());
    }
}
