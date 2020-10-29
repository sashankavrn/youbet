package com.youbetcha.config;

import com.youbetcha.converter.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LoginResponseToLoginConverter());
        registry.addConverter(new PlayerDtoToPlayerConverter());
        registry.addConverter(new PlayerToPlayerDtoConverter());
        registry.addConverter(new RegistrationResponseToDtoConverter());
        registry.addConverter(new PlayerDtoToPlayerReqConverter());
        registry.addConverter(new UserDetailsResponseToUserDetailsDtoConverter());
        registry.addConverter(new DepositTransactionToTransactionDtoConverter());
        registry.addConverter(new WithdrawTransactionToTransactionDtoConverter());
    }

}
