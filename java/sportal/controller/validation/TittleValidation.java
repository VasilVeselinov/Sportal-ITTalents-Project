package sportal.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ArticleTitleValidation.class)
public @interface TittleValidation {

    String message() default "Title have to between 3 and 100 symbols!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
