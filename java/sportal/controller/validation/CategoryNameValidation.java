package sportal.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryNameValidation implements ConstraintValidator<NameValid, String> {

    private static final int MAX_SYMBOLS_OF_TITLE = 30;
    private static final int MIN_SYMBOLS_OF_TITLE = 2;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return name != null && name.length() <= MAX_SYMBOLS_OF_TITLE && name.length() >= MIN_SYMBOLS_OF_TITLE;
    }
}
