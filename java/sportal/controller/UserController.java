package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.pojo.User;
import sportal.model.service.UserService;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/registration")
    public UserResponseDTO registrationUser(@RequestBody UserRegistrationFormDTO userRegFormDTO,
                                            HttpSession session) throws BadRequestException {
        User regUser = this.userService.registration(userRegFormDTO);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, regUser);
        return new UserResponseDTO(regUser);
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                     HttpSession session) throws BadRequestException {
        User logUser = this.userService.login(userLoginFormDTO);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return new UserResponseDTO(logUser);
    }

    @PutMapping(value = "/change_password")
    public UserResponseDTO changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                HttpSession session) {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        User userAfterChangePassword = this.userService.changePassword(userChangePasswordDTO, user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, userAfterChangePassword);
        return new UserResponseDTO(userAfterChangePassword);
    }

    @PostMapping(value = "/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "You are successful logout!";
    }

    // todo this operation should only be performed by the Director of Sportal
    @DeleteMapping(value = "/remove/{" + USER_ID + "}")
    public UserResponseDTO removeUser(@PathVariable(name = USER_ID) long userId,
                                      HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return this.userService.adminRemoveUserByUserId(userId, user);
    }
}
