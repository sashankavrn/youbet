package com.youbetcha.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        int tryCount = 0;
        int maxLimit = 3; //Set your max limit here

        while (!response.isSuccessful() && tryCount < maxLimit) {
            LOGGER.error("Intercept request failed - {}", tryCount);
            tryCount++;
            // retry the request
            response.close();
            response = chain.proceed(request);
        }

        // otherwise just pass the original response on
        return response;
    }
}
