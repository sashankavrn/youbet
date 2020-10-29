package com.youbetcha.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class SpringAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        /*TODO get user*///SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.of("user");
    }
}
