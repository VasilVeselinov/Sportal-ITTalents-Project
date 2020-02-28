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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BasePOJO implements GrantedAuthority {

    private String authority;

    private Role(RoleServiceDTO serviceDTO) {
         this.setId(serviceDTO.getId());
        this.authority = serviceDTO.getAuthority();
    }

   static List<Role> fromDTOToPOJO(List<RoleServiceDTO> authorities) {
       List<Role> roleSet = new ArrayList<>();
        for (RoleServiceDTO serviceDTO : authorities){
            roleSet.add(new Role(serviceDTO));
        }
        return roleSet;
    }
}
