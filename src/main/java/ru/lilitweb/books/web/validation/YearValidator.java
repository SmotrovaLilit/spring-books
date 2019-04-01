package ru.lilitweb.books.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<YearConstraint, Integer> {
    @Override
    public boolean isValid(Integer yearField, ConstraintValidatorContext context) {
        return yearField > 0 && yearField <= LocalDate.now().getYear();
    }
}
