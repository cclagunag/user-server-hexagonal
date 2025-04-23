package com.user.user.infraestructure.adapter.handlers;

import com.user.user.domain.exceptions.ResourceNotFoundException;
import com.user.user.domain.exceptions.UniqueConstraintException;
import com.user.user.infraestructure.adapter.DTOS.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Usuario", 1);
        ErrorResponse response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.status());
        assertEquals("RESOURCE_NOT_FOUND", response.errorCode());
        assertTrue(response.message().contains("Usuario"));
    }

    @Test
    void testHandleConflict() {
        UniqueConstraintException ex = new UniqueConstraintException("Usuario", "email", "test@example.com");
        ErrorResponse response = handler.handleConflict(ex);

        assertEquals(HttpStatus.CONFLICT.value(), response.status());
        assertEquals("UNIQUE_CONSTRAINT_VIOLATION", response.errorCode());
        assertTrue(response.message().contains("Usuario"));
        assertTrue(response.message().contains("email"));
    }

    @Test
    void testHandleValidationException() {
        // Mock de FieldError
        FieldError fieldError1 = new FieldError("object", "username", "must not be blank");
        FieldError fieldError2 = new FieldError("object", "password", "must be at least 6 characters");

        // Mock de BindingResult
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // Mock de MethodArgumentNotValidException
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        ErrorResponse response = handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.status());
        assertEquals("VALIDATION_ERROR", response.errorCode());
        assertTrue(response.message().contains("username"));
        assertTrue(response.message().contains("password"));
    }
}