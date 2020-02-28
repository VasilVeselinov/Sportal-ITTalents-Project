package sportal.model.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.user.UserChangePasswordModel;
import sportal.controller.models.user.UserRegistrationModel;
import sportal.model.db.pojo.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceDTO {

    private long id;
    private String username;
    private String userEmail;
    private String userPassword;
    private String verificationPassword;
    private String newPassword;
    private List<RoleServiceDTO> authorities;

    private boolean isAccountNonExpired;

    private boolean isAccountNonLocked;

    private boolean isCredentialsNonExpired;

    private boolean isEnabled;

    public static List<UserServiceDTO> fromPOJOToDTO(List<User> users) {
        List<UserServiceDTO> dtoList = new ArrayList<>();
        for (User user:users){
            dtoList.add(new UserServiceDTO(user));
        }
        return dtoList;
    }

    public UserServiceDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userEmail = user.getUserEmail();
    }

    public UserServiceDTO(UserRegistrationModel userModel) {
        this.username = userModel.getUsername();
        this.userEmail = userModel.getUserEmail();
        this.userPassword = userModel.getPassword();
        this.verificationPassword = userModel.getVerificationPassword();
    }

    public UserServiceDTO(UserChangePasswordModel userModel) {
        this.userPassword = userModel.getUserPassword();
        this.verificationPassword = userModel.getVerificationPassword();
        this.newPassword = userModel.getNewPassword();
    }

    public UserServiceDTO(long id, String username, String userEmail) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
    }

    public void addAuthority(RoleServiceDTO serviceDTO){
        this.authorities.add(serviceDTO);
    }
}
