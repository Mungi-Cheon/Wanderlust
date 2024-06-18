package com.travel.global.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class OrderException extends HttpStatusCodeException {

    protected OrderException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    @Override
    public String getMessage() {
        return getStatusText();
    }
}

