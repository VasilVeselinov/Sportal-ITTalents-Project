package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import sportal.model.service.dto.UserServiceDTO;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BasePOJO {

    private String username;
    private String password;
    private String userEmail;
    private Boolean isAdmin;
    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> authorities;

    public User(UserServiceDTO serviceDTO) {
        this.setUsername(serviceDTO.getUsername());
        this.setPassword(serviceDTO.getUserPassword());
        this.setUserEmail(serviceDTO.getUserEmail());
        if (serviceDTO.getIsAdmin() != null) {
            this.setIsAdmin(serviceDTO.getIsAdmin());
        } else {
            this.setIsAdmin(false);
        }
    }
}
