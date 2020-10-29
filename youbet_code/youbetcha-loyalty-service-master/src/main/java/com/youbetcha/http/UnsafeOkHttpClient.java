package com.youbetcha.http;

import com.youbetcha.exceptions.UnsafeOkHttpClientException;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Component
public class UnsafeOkHttpClient {

    @Bean(name = "unsafeHttpClient")
    public OkHttpClient okHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                            //do nothing
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                            //do nothing
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true);

            OkHttpClient okHttpClient = builder.build();
            builder.readTimeout(30, TimeUnit.SECONDS);
            builder.interceptors().add(new RetryInterceptor());
            return okHttpClient;
        } catch (GeneralSecurityException e) {
            throw new UnsafeOkHttpClientException(e);
        }
    }
}
