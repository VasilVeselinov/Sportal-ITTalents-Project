package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.model.db.pojo.Role;
import sportal.model.db.repository.RoleRepository;
import sportal.model.service.IRoleService;
import sportal.model.service.dto.RoleServiceDTO;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static sportal.util.GlobalConstants.SUCCESSFUL_SAVE_IN_DB;

@Service
public class RoleServiceImpl implements IRoleService {

    private static final List<Role> ROLES = Arrays.asList(
            new Role("ROOT"),
            new Role("EDITOR"),
            new Role("ADMIN"),
            new Role("USER"));

    @Autowired
    private RoleRepository roleRepository;
    private static final Logger LOGGER = LogManager.getLogger(IRoleService.class);

    @Transactional
    @Override
    public void seedRoleInDB() {
        this.roleRepository.saveAll(ROLES);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
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
