package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.exceptions.IncorrectPassword;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistsException;
import com.example.userauthenticationservice.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler({UserDoesNotExistsException.class, UserExistsException.class, IncorrectPassword.class})
    public ResponseEntity<String> handleExceptions(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
