package ru.lilitweb.books.rest.error.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.ObjectError;
import ru.lilitweb.books.rest.error.ApiValidationError;

public class ObjectErrorToApiValidationError implements Converter<ObjectError, ApiValidationError> {
    @Override
    public ApiValidationError convert(ObjectError source) {
        return ApiValidationError.builder()
                .object(source.getObjectName())
                .message(source.getDefaultMessage())
                .code(source.getCode())
                .build();
    }
}
