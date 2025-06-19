package io.hahn.bookspaceback.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleCustomException_ShouldReturnBadRequestWithMessage_WhenCustomExceptionIsThrown() {
        String expectedMessage = "Test exception message";
        CustomException customException = new CustomException(expectedMessage);

        ResponseEntity<String> result = globalExceptionHandler.handleCustomException(customException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(expectedMessage, result.getBody());

    }

    @Test
    void handleCustomException_ShouldReturnNotFound_WhenCustomExceptionHasNotFoundStatus() {
        String expectedMessage = "Test exception message";
        CustomException customException = new CustomException(expectedMessage, HttpStatus.NOT_FOUND);

        ResponseEntity<String> result = globalExceptionHandler.handleCustomException(customException);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(expectedMessage, result.getBody());
    }

    @Test
    void handleCustomException_ShouldReturnBadRequest_WhenMessageIsNull() {
        CustomException customException = new CustomException(null);

        ResponseEntity<String> result = globalExceptionHandler.handleCustomException(customException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void handleCustomException_ShouldReturnBadRequestWithEmptyMessage_WhenMessageIsEmpty() {
        CustomException customException = new CustomException("");

        ResponseEntity<String> result = globalExceptionHandler.handleCustomException(customException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("", result.getBody());
    }

    @Test
    void handleCustomException_ShouldReturnBadRequestWithLongMessage_WhenMessageIsVeryLong() {
        StringBuilder longMessage = new StringBuilder();
        longMessage.append("a".repeat(10000));
        CustomException customException = new CustomException(longMessage.toString());

        ResponseEntity<String> result = globalExceptionHandler.handleCustomException(customException);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(longMessage.toString(), result.getBody());
    }

}