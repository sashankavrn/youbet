package com.youbetcha.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.youbetcha.model.ClaimBonusDto;
import com.youbetcha.model.AwardBonusResponse;
import okhttp3.Headers;
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
public class BonusClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(BonusClient.class);

    static final String AWARD_BONUS_URL = "%s/vendorbonus/bonus/trigger/claim?domainID=%s&userID=%s&currency=%s&bonusCode=%s";

    private CustomHttpClient customHttpClient;
    Gson gson = new Gson();

    private String hostName;
    private String domainId;

    public BonusClient(CustomHttpClient customHttpClient,
                       @Value("${everymatrix.ubs.host-name}") String hostName,
                       @Value("${everymatrix.game-launch.domain-id}") String domainId) {
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
        this.domainId = domainId;
    }

    public Optional<AwardBonusResponse> claimBonus(ClaimBonusDto claimBonusDto, String acsSessionId) {
        String awardBonusUrl = format(AWARD_BONUS_URL, hostName, domainId, claimBonusDto.getUserID(), claimBonusDto.getCurrency(), claimBonusDto.getBonusCode());
        Map<String, String> postHeaders = new HashMap<>();
        postHeaders.put("AcsSessionId", acsSessionId);
        Headers headers = Headers.of(postHeaders);
        String claimBonusBody = "{\"bonusWalletID\" : \"" + claimBonusDto.getBonusWalletId() + "\"}";
        RequestBody body = RequestBody.Companion.create(gson.toJson(claimBonusBody), JSON);

        return customHttpClient.executePostCall(AwardBonusResponse.class, awardBonusUrl, body, headers,
                "Player award bonus request failed received. {}",
                "Error trying to award bonus");
    }

}
