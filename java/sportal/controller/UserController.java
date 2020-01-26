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
import sportal.model.service.UserService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RestController
//@RequestMapping("/users") // for all mapping start URL
public class UserController extends AbstractController {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;

    @PostMapping(value = "/users")
    public UserResponseDTO registrationUser(@RequestBody UserRegistrationFormDTO userRegFormDTO,
                                            HttpSession session) throws BadRequestException {
        UserResponseDTO userResponseDTO = this.userService.registration(userRegFormDTO, session);
        return userResponseDTO;
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                     HttpSession session) throws BadRequestException {
        UserResponseDTO userResponseDTO = this.userService.login(userLoginFormDTO, session);
        return userResponseDTO;
    }

    @PutMapping(value = "/users/change_password")
    public UserResponseDTO changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                HttpSession session) throws SQLException {
        UserResponseDTO userResponseDTO = this.userService.changePassword(userChangePasswordDTO, session);
        return userResponseDTO;
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
