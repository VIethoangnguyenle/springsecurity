package com.hoang.springauthentication.controller;

import com.hoang.springauthentication.core.dto.RestResponse;
import com.hoang.springauthentication.dto.LoginDto;
import com.hoang.springauthentication.dto.UserDto;
import com.hoang.springauthentication.keycloak.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final KeycloakService keycloakService;

    public UserServiceImpl(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Override
    public RestResponse<Object> createUser(UserDto userDto) {
        String userId = keycloakService.createUser(userDto);
        return new RestResponse<>().success(userId);
    }

    @Override
    public RestResponse<Object> login(LoginDto loginDto) throws VerificationException {
        AccessTokenResponse response =  keycloakService.getJWT(loginDto.getUsername(), loginDto.getPassword());
        return new RestResponse<>().success(response);
    }
}
