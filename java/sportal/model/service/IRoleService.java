package sportal.model.service;

import sportal.model.service.dto.RoleServiceDTO;

import java.util.List;

public interface IRoleService {

    void seedRoleInDB();

    List<RoleServiceDTO> findAll();

    RoleServiceDTO getAuthorities(String authority);
}
