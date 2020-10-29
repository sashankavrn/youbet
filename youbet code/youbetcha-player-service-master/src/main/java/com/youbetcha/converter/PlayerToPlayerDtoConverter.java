package com.youbetcha.converter;

import com.youbetcha.model.Player;
import com.youbetcha.model.dto.PlayerDto;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerToPlayerDtoConverter implements Converter<Player, PlayerDto> {

    @NonNull
    @Override
    public PlayerDto convert(Player player) {
        return PlayerDto.builder()
                .id(player.getId())
                .email(player.getEmail())
                .password(player.getPassword())
                .currency(player.getCurrency())
                .countryCode(player.getCountryCode())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .mobilePrefix(player.getMobilePrefix())
                .mobile(player.getMobile())
                .birthDate(player.getBirthDate())
                .city(player.getCity())
                .address1(player.getAddress1())
                .address2(player.getAddress2())
                .postalCode(player.getPostalCode())
                .signupIp(player.getSignupIp())
                .acceptTCs(player.getAcceptTermsAndConditions())
                .bonusOptIn(player.getBonusOptIn())
                .allowNewsAndOffers(player.getAllowNewsAndOffers())
                .allowSmsNewsAndOffers(player.getAllowSmsNewsAndOffers())
                .build();
    }

}