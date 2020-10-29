package com.youbetcha.client;

import com.youbetcha.model.SessionIdValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@Profile("!ci")
public class LoginClient implements Login {
    static final String VALIDATE_SESSION_URL = "%s/ServerAPI/IsActiveUserSession/%s/%s/%s/%s";

    private String hostName;
    private String version;
    private String partnerId;
    private String partnerKey;

    private CustomHttpClient customHttpClient;

    public LoginClient(CustomHttpClient customHttpClient,
                       @Value("${everymatrix.server-api.host-name}") String hostName,
                       @Value("${everymatrix.server-api.version}") String version,
                       @Value("${everymatrix.server-api.partner-id}") String partnerId,
                       @Value("${everymatrix.server-api.partner-key}") String partnerKey) {
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
        this.version = version;
        this.partnerId = partnerId;
        this.partnerKey = partnerKey;
    }

    @Override
    public Optional<SessionIdValidationResponse> validateSession(String sessionId) {
        String validateCredentials = format(VALIDATE_SESSION_URL, hostName, version, partnerId, partnerKey, sessionId);

        return customHttpClient.executeGetCall(SessionIdValidationResponse.class, validateCredentials,
                "Validating sessionId request failed with {}",
                "Error trying to execute call ");
    }
}
