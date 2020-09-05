package com.example.strangeclass.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    public void validateError(BindingResult result) throws Exception {
        if (result.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError error : result.getFieldErrors()) {
                stringBuilder.append(error.getDefaultMessage()).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            throw new Exception(stringBuilder.toString());
        }
    }
}
