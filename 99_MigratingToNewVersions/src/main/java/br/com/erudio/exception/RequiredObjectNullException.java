package br.com.erudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectNullException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RequiredObjectNullException() {
        super("It is not allowed to persist a null object!");
    }

    public RequiredObjectNullException(String exception) {
        super(exception);
    }
}