package com.youbetcha.client;

import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.response.InitiateResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "everymatrix.money-matrix.host-name=https://core-gm-stage.everymatrix.com",
        "everymatrix.server-api.version=1",
        "everymatrix.server-api.partner-id=id",
        "everymatrix.server-api.partner-key=key",
})
public class PaymentsClientTest {

    PaymentClient paymentClient;
    CustomHttpClient customHttpClient;

    @Value("${everymatrix.money-matrix.host-name}")
    private String hostName;
    @Value("${everymatrix.server-api.version}")
    private String version;
    @Value("${everymatrix.server-api.partner-id}")
    private String partnerId;
    @Value("${everymatrix.server-api.partner-key}")
    private String partnerKey;

    private PodamFactory podamFactory;

    @Before
    public void setUp() throws IOException {
        podamFactory = new PodamFactoryImpl();
        customHttpClient = new CustomHttpClient(MockHttpClient.mockHttpClient("{\"key\": \"val\"}"));
        paymentClient = new PaymentClient(customHttpClient, hostName, hostName, version, partnerId, partnerKey);
    }

    @Test
    public void shouldInitDeposit() {
        Optional<InitiateResponse> initiateResponse = paymentClient.
                initDeposit(podamFactory.manufacturePojo(InitiateRequest.class));

        assertTrue(initiateResponse.isPresent());
    }

    @Test
    public void shouldInitWithdraw() {
        Optional<InitiateResponse> initiateResponse = paymentClient.
                initWithdraw(podamFactory.manufacturePojo(InitiateRequest.class));

        assertTrue(initiateResponse.isPresent());
    }
}
