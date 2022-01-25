package com.hoang.springauthentication.jwtprovider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class JwtProvider {

    private static final String USERNAME = "preferred_username";
    private static final String SUBJECT = "sub";
    private static final String SESSION = "session_state";
    private static final String FULL_NAME = "name";
    private static final String CLIENT_ID = "clientId";
    private static final String AZP = "azp";
    private static final String CIF = "cif";
    private static final String USER_LEVEL = "user_level";
    private static final String REALM_ACCESS = "realm_access";
    private static final String BEARER = "Bearer ";
    private static final String BRANCH = "branch";

    public static List<Object> getRoleFromCurrentToken() {
        HttpServletRequest currentRequest =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String token = currentRequest.getHeader(HttpHeaderNames.AUTHORIZATION);
        LinkedHashMap<String, String> realmAccess = (LinkedHashMap<String, String>) parseClaims(token.substring(7)).get(REALM_ACCESS);
        JSONObject jsonObject = new JSONObject(realmAccess);
        JSONArray array = jsonObject.getJSONArray("roles");
        return array.toList();
    }

    private static Claims parseClaims(String token) {
        try {
            int lastIndexOfDot = token.lastIndexOf('.');
            return (Claims) Jwts.parser()
                    .parse(token.substring(0, lastIndexOfDot + 1))
                    .getBody();
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            log.error("Unsupported or invalid token: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String getUsernameFromToken(String token) {
        return parseClaims(token.substring(7)).get(USERNAME) == null ? null : parseClaims(token.substring(7)).get(USERNAME).toString();
    }
}
