package com.youbetcha.client;

import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
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
public class LoginClientTest {
    LoginClient loginClient;
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
        loginClient = new LoginClient(customHttpClient, hostName, version, partnerId, partnerKey);
    }

    @Test
    public void shouldValidateUserNameAvailable() {
        LoginDto userLogin = TestDataHelper.createLogin();
        Optional<LoginPlayerResponse> loginPlayerResponse
                = loginClient.loginUser(userLogin);

        assertTrue(loginPlayerResponse.isPresent());
    }

    @Test
    public void shouldLogoutUser() {
        Optional<LogoutPlayerResponse> logoutPlayerResponse
                = loginClient.logout("someSessionId");

        assertTrue(logoutPlayerResponse.isPresent());
    }
}
