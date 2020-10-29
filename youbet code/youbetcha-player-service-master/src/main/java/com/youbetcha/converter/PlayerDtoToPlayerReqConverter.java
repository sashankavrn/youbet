package com.youbetcha.converter;

import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerReq;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerDtoToPlayerReqConverter implements Converter<PlayerDto, PlayerReq> {

    @NonNull
    @Override
    public PlayerReq convert(PlayerDto playerDto) {
        PlayerReq playerReq = PlayerReq.builder()
                .userName(playerDto.getUserName())
                .email(playerDto.getEmail())
                .title(playerDto.getTitle())
                .password(playerDto.getPassword())
                .currency(playerDto.getCurrency())
                .countryCode(playerDto.getCountryCode())
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .mobile(playerDto.getMobile())
                .birthDate(playerDto.getBirthDate())
                .city(playerDto.getCity())
                .address1(playerDto.getAddress1())
                .address2(playerDto.getAddress1())
                .postalCode(playerDto.getPostalCode())
                .signupIp(playerDto.getSignupIp())
                .language(playerDto.getLanguage())
                .allowNewsAndOffers(toString(playerDto.getAllowNewsAndOffers()))
                .allowSmsNewsAndOffers(toString(playerDto.getAllowSmsNewsAndOffers()))
                .build();
        if (playerDto.getMobilePrefix().contains("-")) {
        	playerReq.setMobilePrefix("+1");
        } else {
        	playerReq.setMobilePrefix(playerDto.getMobilePrefix());
        }
        return playerReq;
    }

    private String toString(boolean value) {
        return value ? "1" : "0";
    }

}