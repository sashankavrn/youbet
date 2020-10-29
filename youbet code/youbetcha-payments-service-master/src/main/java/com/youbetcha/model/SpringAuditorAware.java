package com.youbetcha.model;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContextHolder.getContext().getAuthentication().getName();
        return Optional.of("user");
    }
}
