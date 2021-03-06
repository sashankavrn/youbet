package com.youbetcha.client;

import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "everymatrix.server-api.host-name=https://core-gm-stage.everymatrix.com",
        "everymatrix.server-api.version=1",
        "everymatrix.server-api.partner-id=id",
        "everymatrix.server-api.partner-key=key",
})
public class PlayerClientTest {
    UserClient userClient;
    CustomHttpClient customHttpClient;

    @Value("${everymatrix.server-api.host-name}")
    private String hostName;
    @Value("${everymatrix.server-api.version}")
    private String version;
    @Value("${everymatrix.server-api.partner-id}")
    private String partnerId;
    @Value("${everymatrix.server-api.partner-key}")
    private String partnerKey;

    @Before
    public void setUp() throws IOException {
        customHttpClient = new CustomHttpClient(MockHttpClient.mockHttpClient("{\"key\": \"val\"}"));
        userClient = new UserClient(customHttpClient, hostName, version, partnerId, partnerKey);
    }

    @Test
    public void shouldGenerateHash() {
        Optional<PlayerHashResponse> userHashResponse
                = userClient.generateUserHash(TestDataHelper.createUserHashDto());

        assertTrue(userHashResponse.isPresent());
    }

    @Test
    public void shouldActivatePlayer() {
        Optional<PlayerActivateResponse> playerActivateResponse
                = userClient.activateUser("somehashkey");

        assertTrue(playerActivateResponse.isPresent());
    }
}
