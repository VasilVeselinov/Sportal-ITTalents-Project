package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.user.NameValidation;
import sportal.controller.validation.user.PasswordValidation;

@NoArgsConstructor
@Getter
@Setter
public class UserChangePasswordModel {

    @NameValidation
    private String userPassword;
    @PasswordValidation
    private String newPassword;
    @PasswordValidation
    private String verificationPassword;
}
