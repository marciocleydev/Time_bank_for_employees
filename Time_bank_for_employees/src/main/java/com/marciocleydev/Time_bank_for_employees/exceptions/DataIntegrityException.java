package com.marciocleydev.Time_bank_for_employees.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(String message) {
        super(message);
    }
}
