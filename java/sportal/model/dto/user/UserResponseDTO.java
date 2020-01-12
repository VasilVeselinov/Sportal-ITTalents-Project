package sportal.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.User;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDTO {

    private long id;
    private String userName;
    private String userEmail;
    private Boolean isAdmin;

    public UserResponseDTO(User user){
        this.setId(user.getId());
        this.setUserName(user.getUserName());
        this.setUserEmail(user.getUserEmail());
        this.setIsAdmin(user.getIsAdmin());
    }
}
