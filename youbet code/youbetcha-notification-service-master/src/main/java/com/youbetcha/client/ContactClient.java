package com.youbetcha.client;

import com.google.gson.Gson;
import com.youbetcha.model.ContactRequest;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ContactClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactClient.class);
    private final OkHttpClient okHttpClient;

    private String url;
    private String apiKey;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    Gson gson = new Gson();

    public ContactClient(@Autowired @Qualifier("safeHttpClient") OkHttpClient okHttpClient,
                       @Value("${sib.contact-url}") String url,
                       @Value("${sib.api-key}") String apiKey) {
        this.okHttpClient = okHttpClient;
        this.apiKey = apiKey;
        this.url = url;
    }

    public Optional<String> addPlayerToNewUserList(ContactRequest req) {
        RequestBody body = RequestBody.Companion.create(gson.toJson(req), JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .addHeader("content-type", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .addHeader("api-key", apiKey)
                .build();

        return execute(
                "adding new player to new contacts list 7 failed with {}",
                "Error trying to add new player to SIB ", request);
    }

    @NotNull
    private Optional<String> execute(String s, String s1, Request request) {
        Call call = okHttpClient.newCall(request);

        Response response;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                String body = Objects.requireNonNull(response.body()).string();
                return Optional.of(body);
            } else {
                LOGGER.error(s, response);
            }
        } catch (IOException e) {
            LOGGER.error(s1, e);
        }
        return Optional.empty();
    }
}
