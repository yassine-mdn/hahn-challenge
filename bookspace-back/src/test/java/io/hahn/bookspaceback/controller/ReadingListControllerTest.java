package io.hahn.bookspaceback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hahn.bookspaceback.dto.BookBarebonesDTO;
import io.hahn.bookspaceback.dto.ReadingListDTO;
import io.hahn.bookspaceback.dto.ReadingListRequestDTO;
import io.hahn.bookspaceback.entity.enums.Status;
import io.hahn.bookspaceback.exception.CustomException;
import io.hahn.bookspaceback.exception.GlobalExceptionHandler;
import io.hahn.bookspaceback.service.ReadingListService;
import io.hahn.bookspaceback.util.PageWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReadingListControllerTest {

    @Mock
    private ReadingListService readingListService;

    @InjectMocks
    private ReadingListController readingListController;

    private MockMvc mockMvc;

    private ReadingListDTO mockReadingListDTO;
    private ReadingListRequestDTO mockReadingListRequestDTO;

    private ObjectMapper objectMapper;
    private final String USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Validator validator = new WebMvcConfigurationSupport().mvcValidator();

        mockMvc = MockMvcBuilders.standaloneSetup(readingListController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        mockReadingListRequestDTO = new ReadingListRequestDTO();
        mockReadingListRequestDTO.setUserName(USERNAME);
        mockReadingListRequestDTO.setBookID(1L);
        mockReadingListRequestDTO.setStatus(Status.READING);
        mockReadingListRequestDTO.setRating(4);

        mockReadingListDTO = new ReadingListDTO();
        mockReadingListDTO.setId(1L);
        mockReadingListDTO.setUserName(USERNAME);
        
        BookBarebonesDTO bookDTO = new BookBarebonesDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setCoverUrl("Test Url");
        
        mockReadingListDTO.setBook(bookDTO);
        mockReadingListDTO.setStatus(Status.READING);
        mockReadingListDTO.setRating(4);
        mockReadingListDTO.setAddedAt(LocalDateTime.now());
    }

    @Test
    void create_ShouldReturnCreatedReadingListDTO_WhenSuccess() throws Exception {
        when(readingListService.create(any(ReadingListRequestDTO.class))).thenReturn(mockReadingListDTO);

        String jsonRequest = objectMapper.writeValueAsString(mockReadingListRequestDTO);
        mockMvc.perform(post("/api/v1/users/{username}/reading-list", USERNAME)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value(USERNAME))
                .andExpect(jsonPath("$.book.id").value(1))
                .andExpect(jsonPath("$.book.title").value("Test Book"))
                .andExpect(jsonPath("$.status").value("READING"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void create_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(mockReadingListRequestDTO);

        when(readingListService.create(any(ReadingListRequestDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(post("/api/v1/users/{username}/reading-list", USERNAME)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_ShouldReturnUpdatedReadingListDTO_WhenSuccess() throws Exception {
        Long id = 1L;
        String jsonRequest = objectMapper.writeValueAsString(mockReadingListRequestDTO);

        when(readingListService.update(eq(id), any(ReadingListRequestDTO.class))).thenReturn(mockReadingListDTO);

        mockMvc.perform(put("/api/v1/users/{username}/reading-list/{id}", USERNAME, id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value(USERNAME))
                .andExpect(jsonPath("$.book.id").value(1))
                .andExpect(jsonPath("$.book.title").value("Test Book"))
                .andExpect(jsonPath("$.status").value("READING"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void update_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;
        String jsonRequest = objectMapper.writeValueAsString(mockReadingListRequestDTO);

        when(readingListService.update(eq(id), any(ReadingListRequestDTO.class))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(put("/api/v1/users/{username}/reading-list/{id}", USERNAME, id)
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_ShouldReturnReadingListDTO_WhenSuccess() throws Exception {
        Long id = 1L;

        when(readingListService.getById(id)).thenReturn(mockReadingListDTO);

        mockMvc.perform(get("/api/v1/users/{username}/reading-list/{id}", USERNAME, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value(USERNAME))
                .andExpect(jsonPath("$.book.id").value(1))
                .andExpect(jsonPath("$.book.title").value("Test Book"))
                .andExpect(jsonPath("$.status").value("READING"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void getById_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;

        when(readingListService.getById(id)).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/users/{username}/reading-list/{id}", USERNAME, id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_ShouldReturnPageOfReadingListDTOs_WhenSuccess() throws Exception {
        PageWrapper<ReadingListDTO> readingListPage = new PageWrapper<>(List.of(mockReadingListDTO));

        when(readingListService.getAllByUser(eq(USERNAME), eq(0), eq(10))).thenReturn(readingListPage);

        mockMvc.perform(get("/api/v1/users/{username}/reading-list?page=0&size=10", USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].userName").value(USERNAME))
                .andExpect(jsonPath("$.content[0].book.id").value(1))
                .andExpect(jsonPath("$.content[0].book.title").value("Test Book"))
                .andExpect(jsonPath("$.content[0].status").value("READING"))
                .andExpect(jsonPath("$.content[0].rating").value(4));
    }

    @Test
    void getAll_ShouldReturnEmptyPage_WhenNoReadingLists() throws Exception {
        PageWrapper<ReadingListDTO> emptyPage = new PageWrapper<>(List.of());

        when(readingListService.getAllByUser(eq(USERNAME), eq(0), eq(10))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/users/{username}/reading-list?page=0&size=10", USERNAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getAll_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        when(readingListService.getAllByUser(eq(USERNAME), eq(0), eq(10))).thenThrow(new CustomException("Error occurred"));

        mockMvc.perform(get("/api/v1/users/{username}/reading-list", USERNAME))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_ShouldReturnNoContent_WhenSuccess() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/v1/users/{username}/reading-list/{id}", USERNAME, id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_ShouldReturnBadRequest_WhenErrorOccurs() throws Exception {
        Long id = 1L;

        doThrow(new CustomException("Error occurred")).when(readingListService).delete(id);

        mockMvc.perform(delete("/api/v1/users/{username}/reading-list/{id}", USERNAME, id))
                .andExpect(status().isBadRequest());
    }

    // TODO: add authorization tests after auth is implemented
}