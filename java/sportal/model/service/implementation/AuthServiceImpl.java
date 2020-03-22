package sportal.model.service.implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.NotExistsObjectException;
import sportal.exception.InvalidInputException;
import sportal.model.db.dao.IUserDAO;
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
import java.util.UUID;

import static sportal.util.GlobalConstants.*;

@Service
public class AuthServiceImpl implements IAuthService {

    // Roles
    private static final String USER_USER_AUTHORITY = "USER";
    private static final String ADMIN_USER_AUTHORITY = "ADMIN";
    private static final String EDITOR_USER_AUTHORITY = "EDITOR";
    private static final String ROOT_USER_AUTHORITY = "ROOT";

    private static final String EXISTS = "There is already a registered user with this name or email!";
    private static final String NO_MORE_ACCESS_RIGHTS = "No more access rights!";
    private static final String FAILED_CREDENTIALS = "Invalid old password!";
    private static final String NOT_EXISTS_USER = "User not found!";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IBCryptService cryptService;
    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    private static final Logger LOGGER = LogManager.getLogger(IAuthService.class);

    @Override
    public void registration(UserServiceDTO serviceDTO) throws SQLException {
        if (this.userRepository.existsUserByUsernameOrUserEmail(serviceDTO.getUsername(), serviceDTO.getUserEmail())) {
            throw new InvalidInputException(EXISTS);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        serviceDTO.addAuthority(this.roleService.getAuthority(USER_USER_AUTHORITY));
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        serviceDTO.setUserPassword(this.cryptService.cryptPassword(serviceDTO.getUserPassword()));
        User user = new User(
                serviceDTO.getUsername(), serviceDTO.getUserPassword(),
                serviceDTO.getUserEmail(), Role.fromDTOToPOJO(serviceDTO.getAuthorities()));
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user = this.userDAO.addUser(user);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
        serviceDTO = new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail(), user.getToken());
        this.eventPublisher.publishEvent(new OnRegistrationCompleteEvent(serviceDTO));
    }

    @Override
    public UserServiceDTO changePassword(UserServiceDTO serviceDTO, long userId) {
        User userDB = this.userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        if (!this.cryptService.checkPassword(serviceDTO.getUserPassword(), userDB.getPassword())) {
            throw new AuthorizationException(FAILED_CREDENTIALS);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        userDB.setPassword(this.cryptService.cryptPassword(serviceDTO.getNewPassword()));
        userDB = this.userRepository.save(userDB);
        LOGGER.info(SUCCESSFUL_SAVE_IN_DB);
        return new UserServiceDTO(userDB.getId(), userDB.getUsername(), userDB.getUserEmail());
    }

    @Override
    public UserServiceDTO findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        UserServiceDTO serviceDTO = new UserServiceDTO(user.getId(), user.getUsername(), user.getUserEmail());
        serviceDTO.setAuthorities(RoleServiceDTO.fromPOJOToDTO(this.userDAO.findAllRolesByUserId(user.getId())));
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
        return serviceDTO;
    }

    @Override
    public void authorise(long userId, List<RoleServiceDTO> editorAuthorities) throws BadRequestException {
        this.userRepository.findById(userId)
                .orElseThrow(() -> new NotExistsObjectException(NOT_EXISTS_USER));
        List<Role> roles = this.userDAO.findAllRolesByUserId(userId);
        if (roles.size() >= editorAuthorities.size() - 1) {
            throw new BadRequestException(NO_MORE_ACCESS_RIGHTS);
        }
        LOGGER.info(SUCCESSFUL_VALIDATION);
        this.userDAO.authorise(userId, this.roleService.getAuthority(ADMIN_USER_AUTHORITY).getId());
        LOGGER.info(SUCCESSFUL_UPDATE_OF_DB);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_EXISTS_USER));
        LOGGER.info(SUCCESSFUL_VALIDATION);
        user.setAuthorities(this.userDAO.findAllRolesByUserId(user.getId()));
        LOGGER.info(SUCCESSFUL_RETRIEVAL);
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
