package com.hoang.springauthentication.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoang.springauthentication.core.dto.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationEntryPoint;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint extends KeycloakAuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    public RestAuthenticationEntryPoint(AdapterDeploymentContext adapterDeploymentContext) {
        super(adapterDeploymentContext);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        RestResponse<Object> responseDto = new RestResponse<>().unauthorized();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getOutputStream().print(mapper.writeValueAsString(responseDto));
    }
}
