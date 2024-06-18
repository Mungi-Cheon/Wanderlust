package com.travel.global.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class UserException extends HttpStatusCodeException {

    protected UserException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

