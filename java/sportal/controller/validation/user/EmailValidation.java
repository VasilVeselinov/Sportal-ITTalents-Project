package sportal.controller.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UserEmailValidation.class)
public @interface EmailValidation {

    String message() default "Email have to between 6 and 40 symbols! " +
            "Example for valid email: email@hostexpansion.countryexpansion!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
