package sportal.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.data_validators.SessionValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.pojo.User;
import sportal.model.repository.UserRepository;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static sportal.controller.AbstractController.*;
import static sportal.model.data_validators.AbstractValidator.LOGGED_USER_KEY_IN_SESSION;
import static sportal.model.data_validators.AbstractValidator.WRONG_REQUEST;

@Service
public class UserService {

    private static final String EXISTS = "There is already a registered user with that name or email!";

    @Autowired
    private UserRepository userRepository;

    public UserResponseDTO registration(UserRegistrationFormDTO userRegFormDTO,
                                        HttpSession session) throws BadRequestException {
        if (userRegFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserRegistrationFormDTO validRegUser = UserValidator.checkForTheValidDataForRegistration(userRegFormDTO);
        if (this.userRepository.existsUserByUserNameOrUserEmail(validRegUser.getUserName(), validRegUser.getUserEmail())) {
            throw new ExistsObjectException(EXISTS);
        }
        User user = new User(validRegUser);
        User regUser = this.userRepository.save(user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, regUser);
        return new UserResponseDTO(regUser);
    }

    public UserResponseDTO login(UserLoginFormDTO userLoginFormDTO, HttpSession session) throws BadRequestException {
        if (userLoginFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserLoginFormDTO validLogUser = UserValidator.checkForTheValidDataForLogin(userLoginFormDTO);
        User user = this.userRepository.getUserByUserName(userLoginFormDTO.getUserName());
        User logUser = UserValidator.checkCredentialsOfUserFromDB(user, validLogUser);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return new UserResponseDTO(logUser);
    }

    public UserResponseDTO changePassword(UserChangePasswordDTO userChangePasswordDTO, HttpSession session) {
        User user = SessionValidator.checkUserIsLogged(session);
        User validUser = UserValidator.checkCredentials(user, userChangePasswordDTO);
        User userAfterChangePassword = this.userRepository.save(validUser);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, userAfterChangePassword);
        return new UserResponseDTO(userAfterChangePassword);
    }

    public UserResponseDTO adminRemoveUserByUserId(long userId, HttpSession session) throws BadRequestException {
        if (userId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        Optional<User> existsUser = this.userRepository.findById(userId);
        if (!existsUser.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (user.getId() == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userRepository.deleteById(userId);
        return new UserResponseDTO(existsUser.get());
    }
}
