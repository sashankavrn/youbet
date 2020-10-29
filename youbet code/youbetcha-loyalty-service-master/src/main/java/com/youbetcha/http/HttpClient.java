package com.youbetcha.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class HttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    @Bean(name = "safeHttpClient")
    public OkHttpClient okHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(LOGGER::info);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.interceptors().add(new RetryInterceptor());
        builder.interceptors().add(logging);
        return builder.build();
    }
}
