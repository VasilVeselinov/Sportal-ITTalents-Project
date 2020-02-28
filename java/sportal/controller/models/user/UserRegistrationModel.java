package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.user.EmailValidation;
import sportal.controller.validation.user.NameValidation;
import sportal.controller.validation.user.PasswordValidation;

@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationModel {

    @NameValidation
    private String username;
    @PasswordValidation
    private String password;
    @PasswordValidation
    private String verificationPassword;
    @EmailValidation
    private String userEmail;
}
