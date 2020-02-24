package sportal.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArticleTitleValidation implements ConstraintValidator<TittleValidation, String> {

    private static final int MAX_SYMBOLS_OF_TITLE = 100;
    private static final int MIN_SYMBOLS_OF_TITLE = 3;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        return title != null && title.length() <= MAX_SYMBOLS_OF_TITLE && title.length() >= MIN_SYMBOLS_OF_TITLE;
    }
}
