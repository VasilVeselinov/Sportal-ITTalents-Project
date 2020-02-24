package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IAuthService;
import sportal.model.service.IBCryptService;
import sportal.model.service.IRoleService;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.validators.UserValidator;

import java.util.HashSet;

import static sportal.model.validators.AbstractValidator.WRONG_REQUEST;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final String EXISTS = "There is already a registered user with that name or email!";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IBCryptService cryptService;

    @Override
    public UserServiceDTO registration(UserServiceDTO serviceDTO) throws BadRequestException {

        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
//        if (true){
//            this.roleService.seedRoleInDB();
//            serviceDTO.setAuthorities(new HashSet<>(this.roleService.findAll()));
//        }
        UserServiceDTO validDTO = UserValidator.checkForTheValidDataForRegistration(serviceDTO);
        if (this.userRepository.existsUserByUsernameOrUserEmail(validDTO.getUsername(), validDTO.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        String cryptPassword = this.cryptService.cryptPassword(serviceDTO.getUserPassword());
        serviceDTO.setUserPassword(cryptPassword);
        User user = new User(validDTO);
        return new UserServiceDTO(this.userRepository.saveAndFlush(user));
    }

    @Override
    public UserServiceDTO login(UserServiceDTO serviceDTO) throws BadRequestException {
        UserServiceDTO validDTO = UserValidator.checkForTheValidDataForLogin(serviceDTO);
        User user = this.userRepository.findByUsername(validDTO.getUsername());
        return UserValidator.checkCredentialsOfUserFromDB(user, validDTO);
    }

    @Override
    public UserServiceDTO changePassword(UserServiceDTO serviceDTO, UserServiceDTO user) {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        UserServiceDTO validUser = UserValidator.checkCredentials(logUser, serviceDTO);
        String cryptPassword = this.cryptService.cryptPassword(serviceDTO.getNewPassword());
        user.setUserPassword(cryptPassword);
        return new UserServiceDTO(this.userRepository.save(new User(validUser)));
    }
}
