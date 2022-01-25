package com.hoang.springauthentication.controller;

import com.hoang.springauthentication.core.dto.RestResponse;
import com.hoang.springauthentication.dto.LoginDto;
import com.hoang.springauthentication.dto.UserDto;
import com.hoang.springauthentication.jwtprovider.JwtProvider;
import com.hoang.springauthentication.keycloak.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.common.VerificationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserController {

    private final KeycloakService keycloakService;
    private final UserService userService;

    public UserController(KeycloakService keycloakService, UserService userService) {
        this.keycloakService = keycloakService;
        this.userService = userService;
    }
//
    @PostMapping("/create")
    public RestResponse<Object> createUser(@RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PostMapping("/login")
    public RestResponse<Object> login(@RequestBody LoginDto loginDto) throws VerificationException {
        log.info("{}", loginDto);
        return userService.login(loginDto);
    }

    @RolesAllowed("user")
    @GetMapping("/te")
    public RestResponse<Object> testRole(HttpServletRequest request, Principal principal) {
        JwtProvider.getRoleFromCurrentToken();
        return new RestResponse<>().success("Helloooooo!!!!!");
    }
}
