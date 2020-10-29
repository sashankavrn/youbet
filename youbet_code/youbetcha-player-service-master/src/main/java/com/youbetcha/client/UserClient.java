package com.youbetcha.client;

import com.google.gson.Gson;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerHashDto;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.model.response.UserDetailsResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@Profile("!ci")
public class UserClient implements User {
    static final String CHANGE_PASSWORD_URL = "%s/ServerAPI/ChangeUserPassword/%s/%s/%s";
    static final String GENERATE_USERHASH_URL = "%s/ServerAPI/GenerateUserHash/%s/%s/%s";
    static final String ACTIVATE_PLAYER_URL = "%s/ServerAPI/ActivatePlayer/%s/%s/%s/%s";
    static final String GET_USERDETAILS_URL = "%s/ServerAPI/GetUserDetails/%s/%s/%s/%s";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    Gson gson = new Gson();
    private String hostName;
    private String version;
    private String partnerId;
    private String partnerKey;

    private CustomHttpClient customHttpClient;

    public UserClient(CustomHttpClient customHttpClient,
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

    public Optional<PlayerHashResponse> generateUserHash(PlayerHashDto playerHashDto) {
        String url = format(GENERATE_USERHASH_URL, hostName, version, partnerId, partnerKey);
        playerHashDto.setURL(url + "/");
        RequestBody body = RequestBody.Companion.create(gson.toJson(playerHashDto), JSON);

        return customHttpClient.executePostCall(PlayerHashResponse.class, url, body,
                "Creating user hash request failed with {}",
                "Error trying to create user hash ");
    }

    public Optional<PlayerActivateResponse> activateUser(String hashKey) {
        String url = format(ACTIVATE_PLAYER_URL, hostName, version, partnerId, partnerKey, hashKey);

        return customHttpClient.executeGetCall(PlayerActivateResponse.class, url,
                "Activating user request failed with {}",
                "Error trying to activate user ");
    }

    public Optional<UserDetailsResponse> userDetails(String sessionId) {
        String url = format(GET_USERDETAILS_URL, hostName, version, partnerId, partnerKey, sessionId);

        return customHttpClient.executeGetCall(UserDetailsResponse.class, url,
                "Getting user details request failed with {}",
                "Error trying to get user details ");
    }

    @Override
    public Optional<ChangePasswordResponse> changePassword(ChangePasswordDto passwordDto) {
        String changePasswordUrl = format(CHANGE_PASSWORD_URL, hostName, version, partnerId, partnerKey);

        RequestBody body = RequestBody.Companion.create(gson.toJson(passwordDto), JSON);

        return customHttpClient.executePostCall(ChangePasswordResponse.class, changePasswordUrl, body,
                "Player password change request failed with {}",
                "Error trying to change password ");
    }
}
