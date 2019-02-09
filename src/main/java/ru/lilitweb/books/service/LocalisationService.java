package ru.lilitweb.books.service;

import java.util.Locale;

public interface LocalisationService {
    public String getMessage(String code, Object[] args);
    public String getMessage(String code);

    Locale getLocale();
}
