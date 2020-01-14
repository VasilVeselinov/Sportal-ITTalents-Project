package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.dao.UserDAO;
import sportal.model.data_validators.SessionValidator;
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

    @PostMapping(value = "/users")
    public UserResponseDTO registrationUser(@RequestBody UserRegistrationFormDTO userRegFormDTO,
                                            HttpSession session) throws SQLException, BadRequestException {
        if (userRegFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserRegistrationFormDTO validRegUser = UserValidator.checkForTheValidDataForRegistration(userRegFormDTO);
        User existsUser = this.userDAO.findUserByUserNameOrEmail(validRegUser);
        if (existsUser != null) {
            throw new ExistsObjectException(EXISTS);
        }
        User user = new User(validRegUser);
        User regUser = this.userDAO.add(user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, regUser);
        return new UserResponseDTO(regUser);
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                     HttpSession session) throws SQLException, BadRequestException {
        if (userLoginFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        UserLoginFormDTO validLogUser = UserValidator.checkForTheValidDataForLogin(userLoginFormDTO);
        User user = this.userDAO.findUserByUserName(validLogUser.getUserName());
        User logUser = UserValidator.checkCredentialsOfUserFromDB(user, validLogUser);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return new UserResponseDTO(logUser);
    }

    @PutMapping(value = "/users/change_password")
    public UserResponseDTO changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                HttpSession session) throws SQLException {
        User user = SessionValidator.checkUserIsLogged(session);
        User validUser = UserValidator.checkCredentials(user, userChangePasswordDTO);
        if (this.userDAO.changePassword(validUser) > 0) {
            session.setAttribute(LOGGED_USER_KEY_IN_SESSION, validUser);
            return new UserResponseDTO(validUser);
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
        User user = SessionValidator.checkUserIsLogged(session);
        SessionValidator.checkUserIsAdmin(user);
        User existsUser = this.userDAO.findUserByUserId(userId);
        if (existsUser == null) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (user.getId() == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userDAO.deleteById(userId);
        return new UserResponseDTO(existsUser);
    }
}
