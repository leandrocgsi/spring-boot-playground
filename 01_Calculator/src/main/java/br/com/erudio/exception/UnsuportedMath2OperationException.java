package br.com.erudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnsuportedMath2OperationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsuportedMath2OperationException(String exception) {
		super(exception);
	}

}
