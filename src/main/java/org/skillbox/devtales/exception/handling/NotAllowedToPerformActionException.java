package org.skillbox.devtales.exception.handling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK)
public class NotAllowedToPerformActionException extends Exception{
}

