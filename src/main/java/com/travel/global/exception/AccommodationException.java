package com.travel.global.exception;

import com.travel.global.exception.type.ErrorType;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class AccommodationException extends HttpStatusCodeException {

    public AccommodationException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public AccommodationException(ErrorType errorType) {
        super(HttpStatusCode.valueOf(errorType.getStatusCode()));
    }


    @Override
    public String getMessage() {
        return getStatusText();
    }
}

