package com.youbetcha.client;

import com.youbetcha.model.response.RegistrationFieldAvailableResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@Profile("!ci")
public class PreRegistrationClient implements PreRegistration {

    static final String USERNAME_AVAILABLE_URL = "%s/ServerAPI/IsUserNameAvailable/%s/%s/%s/%s";
    static final String EMAIL_AVAILABLE_URL = "%s/ServerAPI/IsEmailAvailable/%s/%s/%s/%s";
    static final String ALIAS_AVAILABLE_URL = "%s/ServerAPI/IsAliasAvailable/%s/%s/%s/%s";

    private CustomHttpClient customHttpClient;

    private String hostName;
    private String version;
    private String partnerId;
    private String partnerKey;

    public PreRegistrationClient(CustomHttpClient customHttpClient,
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
    public Optional<RegistrationFieldAvailableResponse> validateUserNameAvailable(String userName) {
        String userNameAvailableUrl = format(USERNAME_AVAILABLE_URL, hostName, version, partnerId, partnerKey, userName);

        return customHttpClient.executeGetCall(RegistrationFieldAvailableResponse.class, userNameAvailableUrl,
                "Validating username availability request failed with {}",
                "Error trying to check username availability");
    }

    @Override
    public Optional<RegistrationFieldAvailableResponse> validateEmailAvailable(String userEmail) {
        String emailAvailableUrl = format(EMAIL_AVAILABLE_URL, hostName, version, partnerId, partnerKey, userEmail);

        return customHttpClient.executeGetCall(RegistrationFieldAvailableResponse.class, emailAvailableUrl,
                "Validating email availability request failed with {}.",
                "Error trying to check username.");
    }

    @Override
    public Optional<RegistrationFieldAvailableResponse> validateAliasAvailable(String alias) {
        String aliasAvailableUrl = format(ALIAS_AVAILABLE_URL, hostName, version, partnerId, partnerKey, alias);

        return customHttpClient.executeGetCall(RegistrationFieldAvailableResponse.class, aliasAvailableUrl,
                "Validating alias availability request failed with {}.",
                "Error trying to check alias.");
    }

}
