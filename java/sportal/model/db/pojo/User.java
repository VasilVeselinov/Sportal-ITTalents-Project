package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import sportal.model.service.dto.UserServiceDTO;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BasePOJO implements UserDetails {

    private String username;
    private String password;
    private String userEmail;
    @Transient
    private List<Role> authorities;

    public User(UserServiceDTO serviceDTO) {
        this.setUsername(serviceDTO.getUsername());
        this.setPassword(serviceDTO.getUserPassword());
        this.setUserEmail(serviceDTO.getUserEmail());
        this.authorities = Role.fromDTOToPOJO(serviceDTO.getAuthorities());
    }

    @Transient
    private boolean isAccountNonExpired;
    @Transient
    private boolean isAccountNonLocked;
    @Transient
    private boolean isCredentialsNonExpired;
    @Transient
    private boolean isEnabled;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
