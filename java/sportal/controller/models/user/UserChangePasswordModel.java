package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.user.PasswordValidation;

@NoArgsConstructor
@Getter
@Setter
public class UserChangePasswordModel {

//    @PasswordValidation
    private String userPassword;
    @PasswordValidation
    private String newPassword;
    @PasswordValidation
    private String verificationPassword;
}
