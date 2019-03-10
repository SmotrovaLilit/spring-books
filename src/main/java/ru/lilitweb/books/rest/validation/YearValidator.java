package ru.lilitweb.books.rest.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<YearConstraint, Integer> {
    /**
     * Validate year
     *
     * @param yearField year
     * @param context   context in which the constraint is evaluated
     * @return boolean
     */
    @Override
    public boolean isValid(Integer yearField, ConstraintValidatorContext context) {
        return yearField > 0 && yearField <= LocalDate.now().getYear();
    }
}
