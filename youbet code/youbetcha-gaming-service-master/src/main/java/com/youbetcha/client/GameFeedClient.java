package com.youbetcha.client;

import com.youbetcha.model.games.MixDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Component
public class GameFeedClient implements GameFeed {

    static final String MIX_DATA_URL = "%s/jsonFeeds/mix/%s?types=%s";
    private static final String GAME = "GAME";
    private static final String TABLE = "TABLE";
    private static final String JACKPOT = "JACKPOT";
    private String hostName;
    private String operatorKey;
    private CustomHttpClient customHttpClient;

    public GameFeedClient(CustomHttpClient customHttpClient,
                          @Value("${everymatrix.casino-engine.host-name}") String hostName,
                          @Value("${everymatrix.casino-engine.operator-key}") String operatorKey) {
        this.operatorKey = operatorKey;
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
    }

    @Override
    public Optional<List<MixDataResponse>> getMixData(String queryUrl) {
        String url = format(MIX_DATA_URL, hostName, operatorKey, queryUrl);
        return customHttpClient.executeGetCallInputStream(MixDataResponse.class, url,
                "Getting mix data request failed with {}.",
                "Error trying to get mix data ");
    }
}
