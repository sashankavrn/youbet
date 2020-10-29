package com.youbetcha.client;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomHttpClient.class);
    private final OkHttpClient okHttpClient;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    Gson gson = new Gson();

    public CustomHttpClient(@Autowired @Qualifier("safeHttpClient") OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    @NotNull
    public <T> Optional<T> executeGetCall(Class<T> respClass, String url, String s, String s1) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return execute(respClass, s, s1, request);
    }

    @NotNull
    public <T> Optional<List<T>> executeGetCallInputStream(Class<T> respClass, String url, String s, String s1) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return executeInputStream(respClass, s, s1, request);
    }

    @NotNull
    public <T> Optional<T> executePostCall(Class<T> respClass, String url, RequestBody body, Headers headers, String s, String s1) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .headers(headers)
                .build();

        return execute(respClass, s, s1, request);
    }

    @NotNull
    public <T> Optional<T> executePostCall(Class<T> respClass, String url, RequestBody body, String s, String s1) {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return execute(respClass, s, s1, request);
    }

    @NotNull
    private <T> Optional<T> execute(Class<T> respClass, String s, String s1, Request request) {
        Call call = okHttpClient.newCall(request);

        Response response;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                T responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), respClass);
                return Optional.of(responseBody);
            } else {
                LOGGER.error(s, response);
            }
        } catch (IOException e) {
            LOGGER.error(s1, e);
        }
        return Optional.empty();
    }

    @NotNull
    private <T> Optional<List<T>> executeInputStream(Class<T> respClass, String s, String s1, Request request) {
        Call call = okHttpClient.newCall(request);
        List<T> jsonList = new ArrayList();
        Response response;
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                //T responseBody = gson.fromJson(Objects.requireNonNull(response.body()).string(), respClass);
                InputStream eventStream = response.body().byteStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(eventStream));
                String line = "";
                while ((line = br.readLine()) != null) {
                    jsonList.add(gson.fromJson(line, respClass));
                }
                return Optional.of(jsonList);
            } else {
                LOGGER.error(s, response);
            }
        } catch (IOException e) {
            LOGGER.error(s1, e);
        }
        return Optional.empty();
    }


    public Optional<String> executeGetNonGsonCall(String url, String s, String s1) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return executeNonJsonCall(s, s1, request);
    }

    private Optional<String> executeNonJsonCall(String s, String s1, Request request) {
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
