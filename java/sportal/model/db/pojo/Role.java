package sportal.model.db.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import sportal.model.service.dto.RoleServiceDTO;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BasePOJO implements GrantedAuthority {

    private String authority;

    private Role(long id, String authority) {
        this.setId(id);
        this.authority = authority;
    }

   public static List<Role> fromDTOToPOJO(List<RoleServiceDTO> authorities) {
        List<Role> roleSet = new ArrayList<>();
        for (RoleServiceDTO serviceDTO : authorities) {
            roleSet.add(new Role(serviceDTO.getId(), serviceDTO.getAuthority()));
        }
        return roleSet;
    }
}
