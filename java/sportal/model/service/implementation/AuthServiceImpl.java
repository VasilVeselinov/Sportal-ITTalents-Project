package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.dao.UserDAO;
import sportal.model.db.pojo.Role;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IAuthService;
import sportal.model.service.IBCryptService;
import sportal.model.service.IRoleService;
import sportal.model.service.dto.RoleServiceDTO;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthService {

    public static final String ROOT_USER_AUTHORITY = "ROOT";
    public static final String ADMIN_USER_AUTHORITY = "ADMIN";
    public static final String EDITOR_USER_AUTHORITY = "EDITOR";
    private static final String USER_USER_AUTHORITY = "USER";

    private static final String EXISTS = "There is already a registered user with that name or email!";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IBCryptService cryptService;
    @Autowired
    private UserDAO userDAO;

    @Override
    public void registration(UserServiceDTO serviceDTO) throws SQLException {
        if (this.userRepository.existsUserByUsernameOrUserEmail(serviceDTO.getUsername(), serviceDTO.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        serviceDTO.addAuthority(this.roleService.getAuthorities(USER_USER_AUTHORITY));
        serviceDTO.setUserPassword(this.cryptService.cryptPassword(serviceDTO.getUserPassword()));
        User user = new User(serviceDTO);
        this.userDAO.addUser(user);
    }

    @Override
    public UserServiceDTO changePassword(UserServiceDTO serviceDTO, UserServiceDTO user) {
        Optional<User> userDB = this.userRepository.findById(user.getId());
        if (!this.cryptService.checkPassword(serviceDTO.getUserPassword(), userDB.get().getPassword())) {
            throw new AuthorizationException(FAILED_CREDENTIALS);
        }
        userDB.get().setPassword(this.cryptService.cryptPassword(serviceDTO.getNewPassword()));
        return new UserServiceDTO(this.userRepository.save(userDB.get()));
    }

    @Override
    public UserServiceDTO findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));

        UserServiceDTO serviceDTO = new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail());
        serviceDTO.setAuthorities(RoleServiceDTO.fromPOJOToDTO(this.userDAO.findAllRolesByUserId(user.getId())));
        return serviceDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_OBJECT));
        user.setAuthorities(this.userDAO.findAllRolesByUserId(user.getId()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }
}
