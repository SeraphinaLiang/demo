package com.example.strangeclass.controller;

import com.example.strangeclass.dto.Reply;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionAdvice extends BaseController {

    @ExceptionHandler(Exception.class)
    protected Reply serviceException(Exception ex) {
        return new Reply<>(false, ex.getMessage());
    }
}
