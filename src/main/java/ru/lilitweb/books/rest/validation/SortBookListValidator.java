package ru.lilitweb.books.rest.validation;

import org.springframework.data.domain.Sort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class SortBookListValidator implements ConstraintValidator<SortBookListConstraint, Sort> {

    /**
     * Validate sort object
     *
     * @param sortValue Sort object
     * @param context   context in which the constraint is evaluated
     * @return boolean
     */
    @Override
    public boolean isValid(Sort sortValue, ConstraintValidatorContext context) {
        if (sortValue == null) {
            return true;
        }

        return sortValue.get().noneMatch(order -> (!Arrays.asList("createdAt", "id")
                .contains(order.getProperty())));
    }
}
