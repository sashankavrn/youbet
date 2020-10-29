package com.youbetcha.service;

import com.youbetcha.exceptions.SignatureHashingFailureException;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.response.StatusChangeResponse;
import com.youbetcha.model.payments.response.StatusCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class SignatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SignatureService.class);

    @Value("${everymatrix.merchant.key}")
    private String merchantKey;

    @Value("${everymatrix.merchant.id}")
    private String merchantId;

    public String signForInitiateRequest(InitiateRequest request) {
        request.setMerchantId(Long.parseLong(merchantId));
        String data = merchantId
                .concat(String.format("MerchantReference='%s';", request.getMerchantReference()))
                .concat("PaymentMethod='';")
                .concat(String.format("CustomerId='%s';", request.getCustomerId()))
                .concat(String.format("Amount='%s';", request.getAmount()))
                .concat(String.format("Currency='%s';", request.getCurrency()))
                .concat(merchantKey);
        LOGGER.info("Signature data: {}", data);
        return sign(data);
    }

    public String signForStatusChange(StatusChangeResponse request) {
        String data = merchantId
                .concat(String.format("Action='%s';", request.getAction()))
                .concat(String.format("ResponseStatus='%s';", request.getResponseStatus()))
                .concat(merchantKey);

        return sign(data);
    }

    public String signForStatusCheck(StatusCheckResponse request) {
        String data = merchantId
                .concat(String.format("Action='%s';", request.getAction()))
                .concat(String.format("ResponseStatus='%s';", request.getResponseStatus()))
                .concat(String.format("TransactionCode='%s';", request.getTransactionCode()))
                .concat(String.format("Status='%s';", request.getStatus()))
                .concat(merchantKey);

        return sign(data);
    }

    private String sign(String data) {
        byte[] secret = merchantKey.getBytes(StandardCharsets.UTF_8);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return hashToString(dataBytes, secret);
    }

    private String hashToString(byte[] data, byte[] key) {
        Mac sha512HMAC;
        try {
            sha512HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA512");
            sha512HMAC.init(secretKey);
            byte[] macData = sha512HMAC.doFinal(data);
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(macData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureHashingFailureException("Failed to hash signature");
        }
    }
}
