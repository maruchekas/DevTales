package org.skillbox.devtales.exception.handling;

import org.skillbox.devtales.exception.UnAuthorisedUserException;
import org.skillbox.devtales.exception.UserAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.skillbox.devtales.config.Constants.ACCESS_DENIED;
import static org.skillbox.devtales.config.Constants.USER_NOT_AUTHORISED;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ResponseEntity<?> handleException(UnAuthorisedUserException unAuthorisedUserException){

        return new ResponseEntity<>(USER_NOT_AUTHORISED, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    ResponseEntity<?> handleException(UserAccessDeniedException userAccessDeniedException){
        return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
    }


}
