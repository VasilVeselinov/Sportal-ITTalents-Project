package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.RoleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserLoginModel {

    private long id;
    private String username;
    private String userEmail;
    private List<RoleServiceDTO> authorities;

    public UserLoginModel(UserServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.username = serviceDTO.getUsername();
        this.userEmail = serviceDTO.getUserEmail();
        this.authorities = serviceDTO.getAuthorities();
    }
}
