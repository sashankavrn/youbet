package com.youbetcha.converter;

import com.youbetcha.model.Player;
import com.youbetcha.model.dto.PlayerDto;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerDtoToPlayerConverter implements Converter<PlayerDto, Player> {

    @NonNull
    @Override
    public Player convert(PlayerDto playerDto) {
        return Player.builder()
                .id(playerDto.getId())
                .email(playerDto.getEmail())
                .password(playerDto.getPassword())
                .currency(playerDto.getCurrency())
                .countryCode(playerDto.getCountryCode())
                .firstName(playerDto.getFirstName())
                .lastName(playerDto.getLastName())
                .mobilePrefix(playerDto.getMobilePrefix())
                .mobile(playerDto.getMobile())
                .birthDate(playerDto.getBirthDate())
                .city(playerDto.getCity())
                .address1(playerDto.getAddress1())
                .postalCode(playerDto.getPostalCode())
                .signupIp(playerDto.getSignupIp())
                .bonusOptIn(playerDto.getBonusOptIn())
                .acceptTermsAndConditions(playerDto.getAcceptTCs())
                .language(playerDto.getLanguage())
                .allowNewsAndOffers(playerDto.getAllowSmsNewsAndOffers())
                .allowSmsNewsAndOffers(playerDto.getAllowSmsNewsAndOffers())
                .enabled(Boolean.TRUE)
                .build();
    }

}
