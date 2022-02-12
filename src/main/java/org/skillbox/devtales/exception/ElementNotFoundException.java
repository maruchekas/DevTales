package org.skillbox.devtales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ElementNotFoundException extends EntityNotFoundException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
