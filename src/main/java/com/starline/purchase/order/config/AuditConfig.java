package com.starline.purchase.order.config;
/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 10/15/2024 9:45 PM
@Last Modified 10/15/2024 9:45 PM
Version 1.0
*/

import com.starline.purchase.order.utils.AuthUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "dateTimeProvider")
public class AuditConfig {

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication) || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("SYSTEM");
            }


            if (authentication.getPrincipal() instanceof Jwt jwt) {
                return Optional.of(AuthUtils.getCurrentUserId(jwt.getClaimAsString("sub")))
                        .map(String::valueOf);
            }
            return Optional.empty();

        };
    }

    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
