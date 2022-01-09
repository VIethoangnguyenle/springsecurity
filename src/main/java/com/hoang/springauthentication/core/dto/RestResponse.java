package com.hoang.springauthentication.core.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RestResponse<T> {

    private T data;

    private String time;

    private int status;

    private String message;

    public RestResponse<T> success(T data) {
        this.data = data;
        this.status = 200;
        this.message = "success";
        return this;
    }

    public RestResponse<T> success() {
        this.status = 200;
        this.message = "success";
        return this;
    }

    public RestResponse<T> badRequest(T data) {
        this.status = 400;
        this.data = data;
        this.message = "bad_request";
        return this;
    }

    public RestResponse<T> badRequest() {
        this.status = 400;
        this.message = "bad_request";
        return this;
    }

    public RestResponse<T> serverError() {
        this.status = 500;
        this.message = "server_error";
        return this;
    }

    public RestResponse<T> unauthorized() {
        this.status = 401;
        this.message = "server_error";
        return this;
    }
}
