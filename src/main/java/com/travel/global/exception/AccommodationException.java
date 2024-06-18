package com.travel.global.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    protected AccommodationException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

