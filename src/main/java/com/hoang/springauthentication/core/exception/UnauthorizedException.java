package com.hoang.springauthentication.core.exception;

import com.hoang.springauthentication.core.dto.RestError;
import com.hoang.springauthentication.core.dto.RestResponse;

public class UnauthorizedException extends RestException{

    public UnauthorizedException(RestError errors) {
        super(errors);
    }

    @Override
    public RestResponse<RestError> getResponse() {
        return new RestResponse<RestError>().unauthorized();
    }
}
