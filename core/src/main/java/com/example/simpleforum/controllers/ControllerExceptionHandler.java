package com.example.simpleforum.controllers;

import com.example.simpleforum.beans.ErrorResponseBean;
import com.example.simpleforum.exceptions.DataNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseBody
    public ErrorResponseBean handleDataNotFound(HttpServletRequest req, Exception ex) {
        return new ErrorResponseBean(req, HttpStatus.NOT_FOUND, ex);
    }
}