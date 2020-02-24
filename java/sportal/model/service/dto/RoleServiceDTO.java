package sportal.model.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.db.pojo.Role;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RoleServiceDTO {

    private long id;
    private String authority;

    public RoleServiceDTO(Role role) {
        this.id = role.getId();
        this.authority = role.getAuthority();
    }

    public static List<RoleServiceDTO> fromPOJOToDTO(List<Role> roleList) {
        List<RoleServiceDTO> dtoList = new ArrayList<>();
        for (Role role : roleList) {
            dtoList.add(new RoleServiceDTO(role));
        }
        return dtoList;
    }
}
