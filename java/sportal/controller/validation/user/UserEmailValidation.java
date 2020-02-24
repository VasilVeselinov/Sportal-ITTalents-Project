package sportal.controller.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidation implements ConstraintValidator<EmailValidation, String> {

    private static final int MIN_NUMBER_OF_SYMBOL = 6;
    private static final int MAX_NUMBER_OF_SYMBOL = 40;
    private static final String SPECIAL_CHARACTER_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (email == null) {
            return false;
        }
        email = email.trim();
        return email.length() >= MIN_NUMBER_OF_SYMBOL && email.length() <= MAX_NUMBER_OF_SYMBOL &&
                email.matches(SPECIAL_CHARACTER_PATTERN);
    }
}
