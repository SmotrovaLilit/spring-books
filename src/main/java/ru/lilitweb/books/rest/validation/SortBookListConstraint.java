package ru.lilitweb.books.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortBookListValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortBookListConstraint {
    String message() default "Invalid sort";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
