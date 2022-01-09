package com.hoang.springauthentication.core.exception;

import com.hoang.springauthentication.core.dto.RestError;
import com.hoang.springauthentication.core.dto.RestResponse;

public abstract class RestException extends RuntimeException{
    private final RestError errors;

    public RestException(RestError errors) {
        this.errors = errors;
    }

    public abstract RestResponse<RestError> getResponse();

    public RestError getErrors() {
        return errors;
    }
}
