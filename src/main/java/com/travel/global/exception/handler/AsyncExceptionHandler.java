package com.travel.global.exception.handler;

import com.travel.global.exception.EmailException;
import com.travel.global.exception.type.ErrorType;
import java.lang.reflect.Method;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        throw new EmailException(ErrorType.SERVER_ERROR);
    }
}
