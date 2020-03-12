package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

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
    private boolean isEnabled;
    private String token;
    @Transient
    private List<Role> authorities;

    public User(String username, String password, String userEmail, List<Role> authorities) {
        this.username = username;
        this.password = password;
        this.userEmail = userEmail;
        this.authorities = authorities;
        this.isEnabled = false;
    }

    @Transient
    private boolean isAccountNonExpired;
    @Transient
    private boolean isAccountNonLocked;
    @Transient
    private boolean isCredentialsNonExpired;

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
}
