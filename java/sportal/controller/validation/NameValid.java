package sportal.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Constraint(validatedBy = CategoryNameValidation.class)
public @interface NameValid {

    String message() default "Name of the category have to between 2 and 30 symbols!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
