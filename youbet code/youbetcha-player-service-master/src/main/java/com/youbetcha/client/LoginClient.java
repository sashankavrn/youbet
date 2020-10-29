package com.youbetcha.client;

import com.google.gson.Gson;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.model.response.PlayerCredentialValidationResponse;
import com.youbetcha.model.response.SessionIdValidationResponse;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.youbetcha.client.RegistrationClient.JSON;
import static java.lang.String.format;

@Component
@Profile("!ci")
public class LoginClient implements Login {

    static final String LOGIN_BY_USERNAME_OR_EMAIL_URL = "%s/ServerAPI/LoginUser/%s/%s/%s";
    static final String VALIDATE_CREDENTIALS_URL = "%s/ServerAPI/IsValidCredentials/%s/%s/%s/%s/%s";
    static final String VALIDATE_SESSION_URL = "%s/ServerAPI/IsActiveUserSession/%s/%s/%s/%s";
    static final String LOGOUT_URL = "%s/ServerAPI/LogoutUser/%s/%s/%s/%s";
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginClient.class);
    Gson gson = new Gson();
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
    public Optional<LoginPlayerResponse> loginUser(LoginDto loginDto) {
        String loginByUserName = format(LOGIN_BY_USERNAME_OR_EMAIL_URL, hostName, version, partnerId, partnerKey);
        loginDto.setPassword(hashPassword(loginDto.getPassword()));
        RequestBody body = RequestBody.Companion.create(gson.toJson(loginDto), JSON);
        return customHttpClient.executePostCall(LoginPlayerResponse.class, loginByUserName, body,
                "Logging in by username or email address request failed with {}.",
                "Error trying to login by username or email ");
    }

    @Override
    public Optional<LogoutPlayerResponse> logout(String sessionId) {
        String logoutUrl = format(LOGOUT_URL, hostName, version, partnerId, partnerKey, sessionId);

        return customHttpClient.executeGetCall(LogoutPlayerResponse.class, logoutUrl,
                "Logging out request failed with {}.",
                "Error trying to log out ");
    }

    @Override
    public Optional<PlayerCredentialValidationResponse> validateCredentials(String email, String password) {
        String validateCredentials = format(VALIDATE_CREDENTIALS_URL, hostName, version, partnerId, partnerKey, email, password);

        return customHttpClient.executeGetCall(PlayerCredentialValidationResponse.class, validateCredentials,
                "Validating credentials request failed with {}",
                "Error trying to execute call ");
    }

    @Override
    public Optional<SessionIdValidationResponse> validateSession(String sessionId) {
        String validateCredentials = format(VALIDATE_SESSION_URL, hostName, version, partnerId, partnerKey, sessionId);

        return customHttpClient.executeGetCall(SessionIdValidationResponse.class, validateCredentials,
                "Validating sessionId request failed with {}",
                "Error trying to execute call ");
    }

    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(password.getBytes());
            return DatatypeConverter.printHexBinary(thedigest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOGGER.error("Error creating MD5 hash", e);
        }
        return password;
    }
}
