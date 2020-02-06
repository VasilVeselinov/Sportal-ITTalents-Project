package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.data_validators.UserValidator;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.pojo.User;
import sportal.model.repository.UserRepository;

import java.util.Optional;

import static sportal.model.data_validators.AbstractValidator.*;

@Service
public class UserService {

    private static final String EXISTS = "There is already a registered user with that name or email!";

    @Autowired
    private UserRepository userRepository;

    public User registration(UserRegistrationFormDTO userRegFormDTO) throws BadRequestException {
        if (userRegFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserRegistrationFormDTO validRegUser = UserValidator.checkForTheValidDataForRegistration(userRegFormDTO);
        if (this.userRepository.existsUserByUserNameOrUserEmail(validRegUser.getUserName(), validRegUser.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        User user = new User(validRegUser);
        return this.userRepository.save(user);
    }

    public User login(UserLoginFormDTO userLoginFormDTO) throws BadRequestException {
        UserLoginFormDTO validLogUser = UserValidator.checkForTheValidDataForLogin(userLoginFormDTO);
        User user = this.userRepository.getUserByUserName(userLoginFormDTO.getUserName());
        return UserValidator.checkCredentialsOfUserFromDB(user, validLogUser);
    }

    public User changePassword(UserChangePasswordDTO userChangePasswordDTO, User user) {
        User logUser = UserValidator.checkUserIsLogged(user);
        User validUser = UserValidator.checkCredentials(logUser, userChangePasswordDTO);
        return this.userRepository.save(validUser);
    }

    public UserResponseDTO adminRemoveUserByUserId(long userId, User user) throws BadRequestException {
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
        return new UserResponseDTO(existsUser.get());
    }
}
