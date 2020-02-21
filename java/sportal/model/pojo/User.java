package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.user.UserRegistrationFormDTO;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String userPassword;
    private String userEmail;
    private Boolean isAdmin;

    public User(UserRegistrationFormDTO userRegistrationFormDTO) {
        this.setUserName(userRegistrationFormDTO.getUserName());
        this.setUserPassword(userRegistrationFormDTO.getUserPassword());
        this.setUserEmail(userRegistrationFormDTO.getUserEmail());
        if (userRegistrationFormDTO.getIsAdmin() != null) {
            this.setIsAdmin(userRegistrationFormDTO.getIsAdmin());
        } else {
            this.setIsAdmin(false);
        }
    }
}
