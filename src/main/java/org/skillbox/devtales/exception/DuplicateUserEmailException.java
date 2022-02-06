package org.skillbox.devtales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateUserEmailException extends Exception {
    public DuplicateUserEmailException(String message) {
        super(message);
    }
}
