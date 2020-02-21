package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.validators.UserValidator;
import sportal.controller.models.user.UserChangePasswordDTO;
import sportal.controller.models.user.UserLoginFormDTO;
import sportal.controller.models.user.UserRegistrationFormDTO;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IUserService;
import sportal.model.service.dto.UserServiceDTO;

import java.util.Optional;

import static sportal.model.validators.AbstractValidator.*;

@Service
public class UserServiceImpl implements IUserService {

    private static final String EXISTS = "There is already a registered user with that name or email!";

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserServiceDTO registration(UserRegistrationFormDTO userRegFormDTO) throws BadRequestException {
        if (userRegFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserRegistrationFormDTO validRegUser = UserValidator.checkForTheValidDataForRegistration(userRegFormDTO);
        if (this.userRepository.existsUserByUserNameOrUserEmail(validRegUser.getUserName(), validRegUser.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        User user = new User(validRegUser);
        return new UserServiceDTO(this.userRepository.save(user));
    }

    @Override
    public User login(UserLoginFormDTO userLoginFormDTO) throws BadRequestException {
        UserLoginFormDTO validLogUser = UserValidator.checkForTheValidDataForLogin(userLoginFormDTO);
        User user = this.userRepository.findByUserName(userLoginFormDTO.getUserName());
        return UserValidator.checkCredentialsOfUserFromDB(user, validLogUser);
    }

    @Override
    public User changePassword(UserChangePasswordDTO userChangePasswordDTO, User user) {
        User logUser = UserValidator.checkUserIsLogged(user);
        User validUser = UserValidator.checkCredentials(logUser, userChangePasswordDTO);
        return this.userRepository.save(validUser);
    }

    @Override
    public UserServiceDTO adminRemoveUserByUserId(long userId, User user) throws BadRequestException {
        if (userId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<User> existsUser = this.userRepository.findById(userId);
        if (!existsUser.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (logUser.getId() == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userRepository.deleteById(userId);

        return new UserServiceDTO(existsUser.get());
    }
}
