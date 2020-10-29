package com.youbetcha.converter;

import com.youbetcha.model.LoginEntity;
import com.youbetcha.model.response.LoginPlayerResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LoginResponseToLoginConverter implements Converter<LoginPlayerResponse, LoginEntity> {
    @Override
    public LoginEntity convert(LoginPlayerResponse source) {
        return LoginEntity.builder()
                .success(source.getSuccess())
                .timestamp(source.getTimestamp())
                .userId(source.getUserId())
                .firstName(source.getFirstName())
                .language(source.getLanguage())
                .lastName(source.getLastName())
                .registrationDate(source.getRegistrationDate())
                .build();
    }
}
