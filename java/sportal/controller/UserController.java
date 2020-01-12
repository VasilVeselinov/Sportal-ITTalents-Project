package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.DAO.UserDAO;
import sportal.model.data_validators.BCryptValidator;
import sportal.model.data_validators.SessionManagerValidator;
import sportal.model.data_validators.UserValidator;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
public class UserController extends AbstractController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserValidator validator;

    @PostMapping(value = "/users")
    public UserResponseDTO registrationUser(@RequestBody UserRegistrationFormDTO userRegFormDTO,
                                            HttpSession session) throws SQLException, BadRequestException {
        if (userRegFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserRegistrationFormDTO validRegUser = this.validator.checkForTheValidDataForRegistration(userRegFormDTO);
        User existsUser = this.userDAO.findUserByUserNameOrEmail(validRegUser);
        if (existsUser != null) {
            throw new ExistsObjectException(EXISTS);
        }
        User user = new User(validRegUser);
        if (this.userDAO.add(user) > 0) {
            session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
            return new UserResponseDTO(user);
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                     HttpSession session) throws SQLException, BadRequestException {
        if (userLoginFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserLoginFormDTO validLogUser = this.validator.checkForTheValidDataForLogin(userLoginFormDTO);
        User user = this.userDAO.findUserByUserName(validLogUser.getUserName());
        if (user == null) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        if (!BCryptValidator.checkPassword(validLogUser.getUserPassword(), user.getUserPassword())) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, user);
        return new UserResponseDTO(user);
    }

    @PutMapping(value = "/users/change_password")
    public UserResponseDTO changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                HttpSession session) throws SQLException {
        User user = SessionManagerValidator.checkUserIsLogged(session);
        if (!this.validator.checkForTheValidDataForUpdate(userChangePasswordDTO)) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        if (!BCryptValidator.checkPassword(userChangePasswordDTO.getUserPassword(), user.getUserPassword())) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        User userWithNewPassword = new User(userChangePasswordDTO);
        userWithNewPassword.setId(user.getId());
        if (this.userDAO.changePassword(userWithNewPassword) > 0) {
            return new UserResponseDTO(user);
        } else {
            throw new SomethingWentWrongException(SOMETHING_WENT_WRONG);
        }
    }

    @PostMapping(value = "/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "You are successful logout!";
    }

    @DeleteMapping(value = "/users/{" + USER_ID + "}")
    public UserResponseDTO deleteUser(@PathVariable(name = USER_ID) long userId,
                                      HttpSession session) throws SQLException, BadRequestException {
        if (userId < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        User user = SessionManagerValidator.checkUserIsLogged(session);
        SessionManagerValidator.checkUserIsAdmin(user);
        User existsUser = this.userDAO.findUserByUserId(userId);
        if (existsUser == null) {
            throw new NotExistsObjectExceptions(NOT_EXISTS_OBJECT);
        }
        this.userDAO.deleteById(userId);
        return new UserResponseDTO(existsUser);
    }
}
