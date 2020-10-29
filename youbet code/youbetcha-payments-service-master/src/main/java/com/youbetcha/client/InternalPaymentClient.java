package com.youbetcha.client;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.Player;

@Component
public class InternalPaymentClient implements InternalPayment {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPaymentClient.class);

	
    public static final okhttp3.MediaType JSON = okhttp3.MediaType.get("application/json; charset=utf-8");
    static final String PLAYERS_ID_URL = "http://%s:%s/%s/api/v1/admin/players/%s";
    static final String PLAYERS_EMAIL_URL = "http://%s:%s/%s/api/v1/admin/players/email/%s";

    Gson gson = new Gson();
    private String hostName;
    private String port;
    private String context;
    
    @Autowired
    private CustomHttpClient customHttpClient;

    public InternalPaymentClient(CustomHttpClient customHttpClient,
                         @Value("${internal.player-service.host-name}") String hostName,
                         @Value("${internal.player-service.port}") String port,
                         @Value("${internal.player-service.context}") String context) {
    	this.hostName = hostName;
        this.port = port;
        this.context = context;
    }
    
    @Override
    public Optional<Player> getPlayer(Long id) {
        String url = String.format(PLAYERS_ID_URL, hostName, port, context, id);

        LOGGER.info("D> URL For calling a player: " + url);
    	return customHttpClient.executeGetCall(Player.class, url,
    			"Getting player request failed with {}.",
                "Error trying to get player");
    }

    @Override
    public Optional<Player> getPlayer(String email) {
        String url = String.format(PLAYERS_EMAIL_URL, hostName, port, context, email);

        LOGGER.info("D> URL For calling a player: " + url);
    	return customHttpClient.executeGetCall(Player.class, url,
    			"Getting player request failed with {}.",
                "Error trying to get player");
    }

}
