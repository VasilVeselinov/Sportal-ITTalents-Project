package sportal.controller.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UserNameValidation.class)
public @interface NameValidation {

    String message() default "User name have to min 8 symbols and max 20 symbols " +
            "and without spaces between the symbols! ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
