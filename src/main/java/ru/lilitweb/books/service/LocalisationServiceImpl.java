package ru.lilitweb.books.service;

import org.springframework.context.MessageSource;

import java.util.Locale;


public class LocalisationServiceImpl implements LocalisationService {

    private MessageSource source;
    private Locale locale;

    public LocalisationServiceImpl(MessageSource source) {
        this.source = source;
        this.locale = Locale.getDefault();
    }

    @Override
    public String getMessage(String code, Object[] args) {
        return source.getMessage(code, args, locale);
    }

    @Override
    public String getMessage(String code) {
        return source.getMessage(code, null, locale);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
}
