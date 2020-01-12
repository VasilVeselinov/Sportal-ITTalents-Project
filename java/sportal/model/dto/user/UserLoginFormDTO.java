package sportal.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserLoginFormDTO {

    private String userName;
    private String userPassword;
    private String verificationPassword;
}
