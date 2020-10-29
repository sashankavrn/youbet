package com.youbetcha.converter;

import com.youbetcha.model.dto.UserDetailsDto;
import com.youbetcha.model.response.UserDetailsResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDetailsResponseToUserDetailsDtoConverter implements Converter<UserDetailsResponse, UserDetailsDto> {

    @NonNull
    @Override
    public UserDetailsDto convert(UserDetailsResponse source) {
        return UserDetailsDto.builder()
                .personalId(source.getPersonalId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .address1(source.getAddress1())
                .city(source.getCity())
                .postalCode(source.getPostalCode())
                .countryCode(source.getCountryCode())
                .mobile(source.getMobile())
                .mobilePrefix(source.getMobilePrefix())
                .birthDate(source.getBirthDate())
                .build();
    }
}
