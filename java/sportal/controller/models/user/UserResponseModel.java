package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.UserServiceDTO;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseModel {

    private long id;
    private String username;
    private String userEmail;
    private Boolean isAdmin;

    public UserResponseModel(UserServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.username = serviceDTO.getUsername();
        this.userEmail = serviceDTO.getUserEmail();
        this.isAdmin = serviceDTO.getIsAdmin();
    }
}
