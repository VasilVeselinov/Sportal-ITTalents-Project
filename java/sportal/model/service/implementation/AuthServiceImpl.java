package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
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
import sportal.model.util.OnRegistrationCompleteEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    // Roles
    private static final String USER_USER_AUTHORITY = "USER";
    private static final String ADMIN_USER_AUTHORITY = "ADMIN";
    private static final String EDITOR_USER_AUTHORITY = "EDITOR";
    private static final String ROOT_USER_AUTHORITY = "ROOT";

    private static final String EXISTS = "There is already a registered user with that name or email!";
    private static final String NO_MORE_ACCESS_RIGHTS = "No more access rights!";
    private static final String FAILED_CREDENTIALS = "Validate your data is failed!";
    private static final String NOT_EXISTS_USER = "User not found!";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IBCryptService cryptService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void registration(UserServiceDTO serviceDTO) throws SQLException {
        if (this.userRepository.existsUserByUsernameOrUserEmail(serviceDTO.getUsername(), serviceDTO.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        serviceDTO.addAuthority(this.roleService.getAuthorities(USER_USER_AUTHORITY));
        serviceDTO.setUserPassword(this.cryptService.cryptPassword(serviceDTO.getUserPassword()));
        User user = new User(serviceDTO);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user = this.userDAO.addUser(user);
        serviceDTO = new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail(), user.getToken());
        this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(serviceDTO));
    }

    @Override
    public UserServiceDTO changePassword(UserServiceDTO serviceDTO, long userId) {
        User userDB = this.userRepository.findById(userId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_USER));;
        if (!this.cryptService.checkPassword(serviceDTO.getUserPassword(), userDB.getPassword())) {
            throw new AuthorizationException(FAILED_CREDENTIALS);
        }
        userDB.setPassword(this.cryptService.cryptPassword(serviceDTO.getNewPassword()));
        userDB = this.userRepository.save(userDB);
        return new UserServiceDTO(userDB.getId(), userDB.getUsername(), userDB.getUserEmail());
    }

    @Override
    public UserServiceDTO findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_USER));
        UserServiceDTO serviceDTO = new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail());
        serviceDTO.setAuthorities(RoleServiceDTO.fromPOJOToDTO(this.userDAO.findAllRolesByUserId(user.getId())));
        return serviceDTO;
    }

    @Override
    public void upAuthority(long userId, List<RoleServiceDTO> editorAuthorities) throws BadRequestException {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new ExistsObjectException(NOT_EXISTS_USER));
        List<Role> roles = this.userDAO.findAllRolesByUserId(userId);
        if (roles.size() >= editorAuthorities.size() - 1) {
            throw new BadRequestException(NO_MORE_ACCESS_RIGHTS);
        }
        this.userDAO.upAuthorityByUserId(userId, this.roleService.getAuthorities(ADMIN_USER_AUTHORITY).getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_EXISTS_USER));
        user.setAuthorities(this.userDAO.findAllRolesByUserId(user.getId()));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                user.getAuthorities());
    }
}
