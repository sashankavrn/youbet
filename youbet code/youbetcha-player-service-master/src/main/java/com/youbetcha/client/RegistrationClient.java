package com.youbetcha.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.youbetcha.model.dto.PlayerReq;
import com.youbetcha.model.dto.UserRoleReq;
import com.youbetcha.model.response.AssignUserRoleResponse;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@Profile("!ci")
public class RegistrationClient implements Registration {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    static final String REGISTER_URL = "%s/ServerAPI/RegisterUser/%s/%s/%s";
    static final String ASSIGN_USER_ROLE_URL = "%s/ServerAPI/AssignUserRole/%s/%s/%s";
    static final String UPDATE_REGISTERED_USER_URL = "%s/ServerAPI/UpdateUserDetails/%s/%s/%s";

    static final Long ROLE_ID = Long.valueOf(35283);

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationClient.class);
    Gson gson = new Gson();

    private String hostName;
    private String version;
    private String partnerId;
    private String partnerKey;

    private CustomHttpClient customHttpClient;

    public RegistrationClient(CustomHttpClient customHttpClient,
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
    public Optional<PlayerRegistrationResponse> registerUser(PlayerReq playerDto) {
        String registrationUrl = format(REGISTER_URL, hostName, version, partnerId, partnerKey);

        //add property activateAccount to playerDto
        JsonElement jsonElement = gson.toJsonTree(playerDto);
        jsonElement.getAsJsonObject().addProperty("activateAccount", "1");
        LOGGER.info("Added activateAccount=\"1\" to the json body");
        RequestBody body = RequestBody.Companion.create(gson.toJson(jsonElement), JSON);

        return customHttpClient.executePostCall(PlayerRegistrationResponse.class, registrationUrl, body,
                "Player registration request failed received. {}",
                "Error trying to register ");
    }

    @Override
    public Optional<PlayerRegistrationResponse> updateRegisteredUser(PlayerReq playerDto) {
        String registrationUrl = format(UPDATE_REGISTERED_USER_URL, hostName, version, partnerId, partnerKey);

        RequestBody body = RequestBody.Companion.create(gson.toJson(playerDto), JSON);

        return customHttpClient.executePostCall(PlayerRegistrationResponse.class, registrationUrl, body,
                "Player update request failed received with {}",
                "Error trying to update ");
    }

    @Override
    public Optional<AssignUserRoleResponse> assignUserRole(String userID) {
        String registrationUrl = format(ASSIGN_USER_ROLE_URL, hostName, version, partnerId, partnerKey);
        UserRoleReq roleReq = new UserRoleReq(ROLE_ID, userID);

        RequestBody body = RequestBody.Companion.create(gson.toJson(roleReq), JSON);

        return customHttpClient.executePostCall(AssignUserRoleResponse.class, registrationUrl, body,
                "Player role assignation request failed received with {}",
                "Error trying to assign player role ");
    }
}
