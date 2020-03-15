package sportal.model.service;

import sportal.model.service.dto.RoleServiceDTO;

import javax.transaction.Transactional;
import java.util.List;

public interface IRoleService {

    @Transactional
    void seedRoleInDB();

    List<RoleServiceDTO> findAll();

    RoleServiceDTO getAuthority(String authority);
}
