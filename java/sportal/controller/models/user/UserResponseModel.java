package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.User;
import sportal.model.service.dto.UserServiceDTO;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseModel {

    private long id;
    private String userName;
    private String userEmail;
    private Boolean isAdmin;

    public UserResponseModel(User user){
        this.setId(user.getId());
        this.setUserName(user.getUserName());
        this.setUserEmail(user.getUserEmail());
        this.setIsAdmin(user.getIsAdmin());
    }

    public UserResponseModel(UserServiceDTO serviceDTO) {
        this.id=serviceDTO.getId();
        this.userName=serviceDTO.getUserName();
        this.userEmail = serviceDTO.getUserEmail();
        this.isAdmin=serviceDTO.getIsAdmin();
    }
}
