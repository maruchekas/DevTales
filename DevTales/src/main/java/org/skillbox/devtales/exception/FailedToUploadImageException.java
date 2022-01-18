package org.skillbox.devtales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FailedToUploadImageException extends EntityNotFoundException {
    public FailedToUploadImageException(String message) {
        super(message);
    }
}
