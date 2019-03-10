package ru.lilitweb.books.rest.error.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.FieldError;
import ru.lilitweb.books.rest.error.ApiValidationError;

public class FieldErrorToApiValidationConverter implements Converter<FieldError, ApiValidationError> {

    @Override
    public ApiValidationError convert(FieldError source) {
        return ApiValidationError.builder()
                .object(source.getObjectName())
                .code(source.getCode())
                .field(source.getField())
                .message(source.getDefaultMessage())
                .rejectedValue(source.getRejectedValue())
                .build();
    }
}
