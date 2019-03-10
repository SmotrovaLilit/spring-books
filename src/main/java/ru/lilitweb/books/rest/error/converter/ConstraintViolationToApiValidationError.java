package ru.lilitweb.books.rest.error.converter;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.convert.converter.Converter;
import ru.lilitweb.books.rest.error.ApiValidationError;

import javax.validation.ConstraintViolation;

public class ConstraintViolationToApiValidationError implements Converter<ConstraintViolation<?>, ApiValidationError> {

    @Override
    public ApiValidationError convert(ConstraintViolation<?> source) {
        return ApiValidationError.builder()
                .object(source.getRootBeanClass().getSimpleName())
                .field(((PathImpl) source.getPropertyPath()).getLeafNode().asString())
                .rejectedValue(source.getInvalidValue())
                .message(source.getMessage())
                .build();
    }
}
