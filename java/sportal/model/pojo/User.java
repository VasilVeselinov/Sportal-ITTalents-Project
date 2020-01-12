package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.data_validators.BCryptValidator;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;

@NoArgsConstructor
@Getter
@Setter
public class User {

    private long id;
    private String userName;
    private String userPassword;
    private String userEmail;
    private Boolean isAdmin;

    public User(UserRegistrationFormDTO userRegistrationFormDTO){
        this.setUserName(userRegistrationFormDTO.getUserName());
        String cryptPassword = BCryptValidator.cryptPassword(userRegistrationFormDTO.getUserPassword());
        this.setUserPassword(cryptPassword);
        this.setUserEmail(userRegistrationFormDTO.getUserEmail());
        if (userRegistrationFormDTO.getIsAdmin() != null) {
            this.setIsAdmin(userRegistrationFormDTO.getIsAdmin());
        }else {
            this.setIsAdmin(false);
        }
    }

    public User(UserChangePasswordDTO userChangePasswordDTO){
        String cryptPassword = BCryptValidator.cryptPassword(userChangePasswordDTO.getNewPassword());
        this.setUserPassword(cryptPassword);
    }
}
