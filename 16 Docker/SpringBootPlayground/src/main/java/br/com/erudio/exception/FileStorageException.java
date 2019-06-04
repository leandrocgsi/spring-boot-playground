package br.com.erudio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class FileStorageException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public FileStorageException(String exception) {
		super(exception);
	}
	
    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
	
}