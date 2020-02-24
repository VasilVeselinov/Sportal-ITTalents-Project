package sportal.controller.validation.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UserPasswordValidation.class)
public @interface PasswordValidation {

    String message() default "Your password is not strong!\n" +
            "Your password have to min 8 symbols and max 100 symbols,\n" +
            "min one digit between 0 and 9,\n" +
            "min one lowercase letter,\n" +
            "min one uppercase letter,\n" +
            "min one special character between [@#$%^&+=]\n" +
            "and without spaces between the symbols!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
