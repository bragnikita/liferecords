package jp.bragnikita.liferecords.backend.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageException extends RuntimeException {

    private String path;

    public StorageException(String message, String file) {
        super(String.format(message, file));
        this.path = file;
    }

    public StorageException(String message, String file, Throwable cause) {
        this(message, file);
        initCause(cause);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }
}
