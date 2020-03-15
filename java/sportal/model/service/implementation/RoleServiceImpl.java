package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.db.pojo.Role;
import sportal.model.db.repository.RoleRepository;
import sportal.model.service.IRoleService;
import sportal.model.service.dto.RoleServiceDTO;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    private static final List<Role> ROLES = Arrays.asList(
            new Role("ROOT"),
            new Role("EDITOR"),
            new Role("ADMIN"),
            new Role("USER"));

    @Transactional
    @Override
    public void seedRoleInDB() {
        this.roleRepository.saveAll(ROLES);
    }

    @Override
    public List<RoleServiceDTO> findAll() {
        List<Role> roleList = this.roleRepository.findAll();
        return RoleServiceDTO.fromPOJOToDTO(roleList);
    }

    @Override
    public RoleServiceDTO getAuthority(String authority) {
        return new RoleServiceDTO(this.roleRepository.findByAuthority(authority));
    }
}
