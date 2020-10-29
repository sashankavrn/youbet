package com.youbetcha.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.youbetcha.converter.DepositBonusRequestToDepositBonus;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
    	registry.addConverter(new DepositBonusRequestToDepositBonus());
    }

}
