package com.xtremax.clinic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class QueueLimitReachedException extends Exception {
    public QueueLimitReachedException(String message) {
        super(message);
    }
}
