package com.youbetcha.client;

import com.youbetcha.model.games.MixDataResponse;

import java.util.List;
import java.util.Optional;

public interface GameFeed {
    Optional<List<MixDataResponse>> getMixData(String queryUrl);
}
