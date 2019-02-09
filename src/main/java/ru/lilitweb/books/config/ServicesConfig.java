package ru.lilitweb.books.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lilitweb.books.service.LocalisationService;
import ru.lilitweb.books.service.LocalisationServiceImpl;

@Configuration
public class ServicesConfig {

    @Bean
    public LocalisationService localisationService(MessageSource source) {
        return new LocalisationServiceImpl(source);
    }
}
