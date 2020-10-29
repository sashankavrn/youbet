package com.youbetcha.client;

import com.google.gson.Gson;
import com.youbetcha.model.AcsAuthenticateResponse;
import com.youbetcha.model.AwardBonusResponse;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.youbetcha.client.CustomHttpClient.JSON;
import static java.lang.String.format;

@Component
public class AcsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcsClient.class);

    static final String AUTHENTICATE_URL = "%s/loginApp";

    private CustomHttpClient customHttpClient;

    private String hostName;
    private String userName;
    private String password;

    public AcsClient(CustomHttpClient customHttpClient,
                       @Value("${everymatrix.acs.host-name}") String hostName,
                       @Value("${everymatrix.acs.username}") String username,
                       @Value("${everymatrix.acs.password}") String password) {
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
        this.userName = username;
        this.password = password;
    }

    public Optional<AcsAuthenticateResponse> authenticate() {
        String authenticateUrl = format(AUTHENTICATE_URL, hostName);
        String body = "{username:\"" + userName + "\", password:\"" + password + "\"}";

        RequestBody requestBody = RequestBody.Companion.create(body, JSON);

        Map<String, String> postHeaders = new HashMap<>();
        postHeaders.put("Content-Type", "application/x-www-form-urlencoded");
        Headers headers = Headers.of(postHeaders);

        return customHttpClient.executePostCall(AcsAuthenticateResponse.class, authenticateUrl, requestBody, headers,
                "Authenticated successfully. {}",
                "Error trying to authenticate");
    }
}
