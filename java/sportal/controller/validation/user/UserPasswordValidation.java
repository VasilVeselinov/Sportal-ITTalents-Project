package sportal.controller.validation.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPasswordValidation implements ConstraintValidator<PasswordValidation, String> {

    private static final String SPECIAL_CHARACTER_PATTERN =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$%^=!])(?=\\S+$).{8,100}";
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null) {
            return false;
        }
        password = password.trim();
        return password.matches(SPECIAL_CHARACTER_PATTERN);
    }
}
