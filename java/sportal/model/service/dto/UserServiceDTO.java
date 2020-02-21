package sportal.model.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.db.pojo.User;

@Getter
@Setter
public class UserServiceDTO {

    private long id;
    private String userName;
    private String userEmail;
    private Boolean isAdmin;

    public UserServiceDTO(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.userEmail = user.getUserEmail();
        this.isAdmin = user.getIsAdmin();
    }
}
