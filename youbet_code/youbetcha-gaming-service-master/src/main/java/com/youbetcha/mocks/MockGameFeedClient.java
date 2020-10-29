package com.youbetcha.mocks;

import com.youbetcha.client.GameFeed;
import com.youbetcha.model.games.MixDataResponse;

import java.util.List;
import java.util.Optional;

//@Component
//@Profile("ci")
public class MockGameFeedClient implements GameFeed {

    @Override
    public Optional<List<MixDataResponse>> getMixData(String queryUrl) {
        return Optional.empty();
    }
}
