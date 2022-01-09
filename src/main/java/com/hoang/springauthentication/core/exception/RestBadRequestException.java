package com.hoang.springauthentication.core.exception;

import com.hoang.springauthentication.core.dto.RestError;
import com.hoang.springauthentication.core.dto.RestResponse;

public class RestBadRequestException extends RestException{
    public RestBadRequestException(RestError errors) {
        super(errors);
    }

    @Override
    public RestResponse<RestError> getResponse() {
        return new RestResponse<RestError>().badRequest();
    }
}
