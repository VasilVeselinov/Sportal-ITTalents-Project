package sportal.model.service.dto;

import lombok.Getter;
import lombok.Setter;
import sportal.controller.models.user.UserChangePasswordModel;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.models.user.UserRegistrationModel;
import sportal.model.db.pojo.User;

import java.util.Set;

@Getter
@Setter
public class UserServiceDTO {

    private long id;
    private String username;
    private String userEmail;
    private String userPassword;
    private String verificationPassword;
    private String newPassword;
    private Boolean isAdmin;
    private Set<RoleServiceDTO> authorities;

    public UserServiceDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userEmail = user.getUserEmail();
        this.isAdmin = user.getIsAdmin();
    }

    public UserServiceDTO(UserRegistrationModel userModel) {
        this.username = userModel.getUsername();
        this.userEmail = userModel.getUserEmail();
        this.userPassword = userModel.getUserPassword();
        this.verificationPassword = userModel.getVerificationPassword();
        this.isAdmin = userModel.getIsAdmin();
    }

    public UserServiceDTO(UserLoginModel userModel) {
        this.username = userModel.getUsername();
        this.userPassword = userModel.getUserPassword();
        this.verificationPassword = userModel.getVerificationPassword();
    }

    public UserServiceDTO(UserChangePasswordModel userModel) {
        this.userPassword = userModel.getUserPassword();
        this.verificationPassword = userModel.getVerificationPassword();
        this.newPassword = userModel.getNewPassword();
    }

    public UserServiceDTO(long id,  String username, String userEmail, Boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.isAdmin = isAdmin;
    }
}
