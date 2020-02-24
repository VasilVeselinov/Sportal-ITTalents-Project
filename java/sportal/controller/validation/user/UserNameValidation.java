package sportal.controller.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserNameValidation implements ConstraintValidator<NameValidation, String> {

    private static final int MIN_NUMBER_OF_SYMBOLS = 8;
    private static final int MAX_NUMBER_OF_SYMBOLS = 20;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (username == null){
            return false;
        }
        username = username.trim();
        return username.length() >= MIN_NUMBER_OF_SYMBOLS && username.length() <= MAX_NUMBER_OF_SYMBOLS;
    }
}
