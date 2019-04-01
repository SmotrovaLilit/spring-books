package ru.lilitweb.books.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lilitweb.books.service.LocalisationService;
import ru.lilitweb.books.service.LocalisationServiceImpl;

/**
 * Uses for shell
 */
@Configuration
public class LocalisationConfig {
    @Bean
    public LocalisationService localisationService(@Qualifier("messageSource") MessageSource source) {
        return new LocalisationServiceImpl(source);
    }
}
