package com.youbetcha.service;

import com.youbetcha.client.Payment;
import com.youbetcha.model.payments.InitiateType;
import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.payments.dto.InitiateDto;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.repository.TransactionRepository;
import com.youbetcha.service.assembler.Assembler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentsServiceTest {

    @Mock
    private SignatureService signatureService;

    @Mock
    private Payment paymentClient;

    @Mock
    private Assembler assembler;

    @Mock
    private TransactionRepository repository;

    @InjectMocks
    private PaymentsService service;

    private PodamFactory factory;

    @Before
    public void setUp() {
        factory = new PodamFactoryImpl();
    }

    @Ignore
    @Test
    public void shouldInitRequest() {
        stubPaymentServiceMethods();
        InitiateDto dto = factory.manufacturePojo(InitiateDto.class);
        dto.setInitiateType(InitiateType.DEPOSIT);

        CompletableFuture<InitiateResponse> initiateResponseCompletableFuture = service.initRequest("", dto);

        Assert.assertNotNull(initiateResponseCompletableFuture);
    }

    private void stubPaymentServiceMethods() {
//        when(assembler.fetchAndAssemble(any())).thenReturn(new InitiateRequest());
        when(signatureService.signForInitiateRequest(any())).thenReturn(factory.manufacturePojo(String.class));
        when(paymentClient.initDeposit(any())).thenReturn(Optional.of(factory.manufacturePojo(InitiateResponse.class)));
        when(assembler.assembleTransaction(any(), any(), any(), any())).thenReturn(new Transaction());
        when(repository.save(any())).thenReturn(new Transaction());
    }
}
