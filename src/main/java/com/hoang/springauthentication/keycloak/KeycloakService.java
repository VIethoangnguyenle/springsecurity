package com.hoang.springauthentication.keycloak;

import com.hoang.springauthentication.dto.UserDto;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessTokenResponse;

import java.util.List;

public interface KeycloakService {

    String createUser(UserDto user);

    AccessTokenResponse getJWT(String userName, String password) throws VerificationException;

    List<String> getUserRoles(String userId);
}
