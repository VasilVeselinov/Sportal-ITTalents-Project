package sportal.model.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String token;
    private String userPassword;
    private String newPassword;
    private List<RoleServiceDTO> authorities;

    public UserServiceDTO(long id, String username, String userEmail) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
    }

    public UserServiceDTO(long id, String username, String userEmail, String token) {
        this(id, username, userEmail);
        this.token = token;
    }

    public static List<UserServiceDTO> fromPOJOToDTO(List<User> users) {
        List<UserServiceDTO> dtoList = new ArrayList<>();
        for (User user : users) {
            dtoList.add(new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail()));
        }
        return dtoList;
    }

    public UserServiceDTO(String username, String userEmail, String userPassword) {
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public UserServiceDTO(String userPassword, String newPassword) {
        this.userPassword = userPassword;
        this.newPassword = newPassword;
    }

    public void addAuthority(RoleServiceDTO serviceDTO) {
        this.authorities = new ArrayList<>();
        this.authorities.add(serviceDTO);
    }
}
