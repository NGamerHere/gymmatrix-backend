package com.coderstack.gymmatrix.handler;

import com.coderstack.gymmatrix.exceptions.BadRequestException;
import com.coderstack.gymmatrix.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        //response.put("path", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", ex.getMessage());
        //response.put("path", request.getDescription(false).substring(4));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
