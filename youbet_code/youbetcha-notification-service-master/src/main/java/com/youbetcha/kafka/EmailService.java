package com.youbetcha.kafka;

import com.youbetcha.client.ContactClient;
import com.youbetcha.client.EmailClient;
import com.youbetcha.client.InternalClient;
import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final long successfulRegistrationTemplateID = 10;
    private static final long resetPasswordTemplateID = 15;
    private static final long successfullyResetPasswordTemplateID = 17;
    private static final int newClientsListID = 7; //Located under new clients folder in Sendinblue

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private ContactClient contactClient;

    @Autowired
    private InternalClient internalClient;

    public void updateContactList(NotificationDto dto) {// Add or update new player contact to Sendinblue new user list
        logger.info("updateContact()");
        //Obtain Player details from player-service by player email address
        Player player = internalClient.getPlayer(dto.getEmail()).orElseThrow(() -> new PlayerNotFoundException("Could not find player for email: " + dto.getEmail()));
        logger.info("player.getEmail() = " + player.getEmail());

        //Create or update player Contact
        ContactRequest contactRequest = buildContactRequest(dto, player);
        contactClient.addPlayerToNewUserList(contactRequest);//error trying to add or update new clients list
    }

    // ID 10 in SIB
    public void verifyPlayer(NotificationDto dto) {
        logger.info("verifyPlayer()");
        final String verificationLink = dto.getAppUrl() + "/registrationConfirm?token=" + dto.getToken();
        EmailRequest request = buildEmailRequestWithUrl(dto, Collections.singletonMap("VERIFICATIONLINK", verificationLink), successfulRegistrationTemplateID);

        emailClient.sendConfirmationEmail(request);

        updateContactList(dto);//creates new contact in new contacts list
    }

    //ID 15 in SIB
    public void resetPasswordEvent(NotificationDto dto) {
        logger.info("resetPasswordEvent");
        final String confirmationUrl = dto.getAppUrl() + "/resetPassword?token=" + dto.getToken();
        EmailRequest request = buildEmailRequestWithUrl(dto, Collections.singletonMap("URL", confirmationUrl), resetPasswordTemplateID);
        emailClient.sendConfirmationEmail(request);
    }

    // ID 17 in SIB
    public void changePasswordSuccess(NotificationDto dto) {
        logger.info("changePasswordSuccess");
        EmailRequest request = buildEmailRequest(dto, successfullyResetPasswordTemplateID);
        emailClient.sendConfirmationEmail(request);
    }

    private EmailRequest buildEmailRequestWithUrl(NotificationDto dto, Map<String, String> params, long templateID) {
        return EmailRequest.builder()
                .to(Collections.singletonList(Person.builder().email(dto.getEmail()).build()))
                .params(params)
                .templateId(templateID)
                .build();
    }

    private EmailRequest buildEmailRequest(NotificationDto dto, long templateID) {
        return EmailRequest.builder()
                .to(Collections.singletonList(Person.builder().email(dto.getEmail()).build()))
                .templateId(templateID)
                .build();
    }

    private ContactRequest buildContactRequest(NotificationDto dto, Player player) {
        logger.info("SIB buildContactRequest email = " + dto.getEmail());

        Map<String, String> map = new HashMap<String, String>();
        map.put("LASTNAME", player.getLastName());
        map.put("FIRSTNAME", player.getFirstName());
        map.put("SMS", "");
        map.put("BDAY", player.getBirthDate());
        map.put("SIGN_UP_DATE", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        map.put("COUNTRY", player.getCountryCode());
        map.put("CURRENCY", player.getCurrency());
        map.put("BONUS_OPT_IN", (player.isBonusOptIn() ? "Yes" : "No"));
        map.put("PHONE", player.getMobilePrefix() + player.getMobile());

        int[] listIds = {newClientsListID};// Sendinblue new clients list has id
        String[] smtpBlacklistSender = new String[0];
        return ContactRequest.builder()
                .email(dto.getEmail())
                .attributes(Collections.unmodifiableMap(map))
                .emailBlacklisted(!(player.isAllowNewsAndOffers()))
                .smsBlacklisted(!(player.isAllowSmsNewsAndOffers()))
                .listIds(listIds)
                .updateEnabled(true)//Facilitate to update the existing contact in the same request
                .smtpBlacklistSender(smtpBlacklistSender)//SIB documentation: "transactional email forbidden sender for contact. Use only for email Contact ( only available if updateEnabled = true )"
                .build();
    }
}