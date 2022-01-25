package com.hoang.springauthentication.keycloak;

import com.hoang.springauthentication.core.dto.RestError;
import com.hoang.springauthentication.core.exception.RestBadRequestException;
import com.hoang.springauthentication.core.exception.RestException;
import com.hoang.springauthentication.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.TokenVerifier;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KeyCloakServiceImplement implements KeycloakService{

    @Value("#{'${keycloak.realm}'.trim()}")
    private String realm;

    @Value("#{'${keycloak.auth-server-url}'.trim()}")
    private String keycloakAuthUrl;

    @Value("#{'${keycloak.resource}'.trim()}")
    private String keycloakClient;

    @Value("#{'${keycloak.credentials.secret}'.trim()}")
    private String secretKey;

    @Value("#{'${keycloak-admin.username}'.trim()}")
    private String keycloakAdminUsername;

    @Value("#{'${keycloak-admin.password}'.trim()}")
    private String getKeycloakAdminPassword;

    Keycloak keycloakAdmin;

    private String resourceId;

//    Log in Keycloak by admin
    @Bean
    private void initKeycloak() {
        keycloakAdmin = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthUrl)
                .realm("master")
                .clientId("admin-cli")
                .username(keycloakAdminUsername)
                .password(getKeycloakAdminPassword)
                .resteasyClient(
                        new ResteasyClientBuilder()
                                .connectionPoolSize(10).build()
                )
                .build();
        resourceId = keycloakAdmin.realm(realm).clients().findByClientId(keycloakClient).get(0).getId();
    }

    @Override
    public String createUser(UserDto user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setUsername(user.getUserName());
        userRepresentation.setEmail(user.getEmail());

        RealmResource resource = keycloakAdmin.realm(realm);
        UsersResource usersResource = resource.users();

        try (Response response = usersResource.create(userRepresentation)) {
            if (response.getStatus() == 201) {

                String userId = CreatedResponseUtil.getCreatedId(response);

                CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                credentialRepresentation.setTemporary(false);
                credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                credentialRepresentation.setValue(user.getPassword());

                UserResource userResource = usersResource.get(userId);
                userResource.resetPassword(credentialRepresentation);

                return userId;
            } else if (response.getStatus() == 409) {
                log.info("vo day ne sdasd");
                throw new RestBadRequestException(RestError.newBuilder().addUsedField("phoneNumber").build());
            } else {
                log.info("vo day ne");
                log.info("{}", response.getStatusInfo());
                throw new RestBadRequestException(RestError.newBuilder().addMessage(response.getStatusInfo().getReasonPhrase()).build());
            }
        } catch (RestException ex) {
            throw ex;
        } catch (Exception ex) {
            log.info("vo day ne fsfd");
            ex.printStackTrace();
            throw new RestBadRequestException(RestError.newBuilder().addMessage(ex.getMessage()).build());
        }
    }

    @Override
    public AccessTokenResponse getJWT(String userName, String password) throws VerificationException {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakAuthUrl)
                .realm(realm)
                .username(userName)
                .password(password)
                .grantType("password")
                .clientId(keycloakClient)
                .clientSecret(secretKey)
                .build();
        AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
        AccessToken token = TokenVerifier.create(accessTokenResponse.getToken(),AccessToken.class).getToken();
        log.info("{}", token.getRealmAccess().getRoles());
        return accessTokenResponse;
    }

    @Override
    public List<String> getUserRoles(String userId) {
        return keycloakAdmin.realm(realm).users().get(userId).roles().clientLevel(resourceId).listAll()
                .stream().map(RoleRepresentation::getName).collect(Collectors.toList());
    }
}
