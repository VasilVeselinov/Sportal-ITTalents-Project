package sportal.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TextFieldValidation implements ConstraintValidator<TextValidation, String> {

    @Override
    public boolean isValid(String text, ConstraintValidatorContext constraintValidatorContext) {
        return text != null && !text.isEmpty();
    }
}
