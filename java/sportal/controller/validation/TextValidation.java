package sportal.controller.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TextFieldValidation.class)
public @interface TextValidation {

    String message() default "Text field is empty!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
