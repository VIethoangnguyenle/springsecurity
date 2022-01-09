package com.hoang.springauthentication.controller;

import com.hoang.springauthentication.core.dto.RestResponse;
import com.hoang.springauthentication.dto.LoginDto;
import com.hoang.springauthentication.dto.UserDto;
import org.keycloak.common.VerificationException;

public interface UserService {
    RestResponse<Object> createUser(UserDto userDto);

    RestResponse<Object> login(LoginDto loginDto) throws VerificationException;
}
