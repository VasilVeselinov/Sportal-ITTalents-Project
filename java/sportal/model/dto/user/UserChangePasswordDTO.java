package sportal.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserChangePasswordDTO {

    private String userPassword;
    private String newPassword;
    private String verificationPassword;
}
