package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.controller.models.user.UserLoginFormDTO;
import sportal.controller.models.user.UserRegistrationFormDTO;
import sportal.controller.models.user.UserResponseModel;
import sportal.controller.models.user.UserChangePasswordDTO;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.service.implementation.UserServiceImpl;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/registration")
    public UserResponseModel registrationUser(
            @RequestBody UserRegistrationFormDTO userRegFormDTO) throws BadRequestException {
        UserServiceDTO regUser = this.userService.registration(userRegFormDTO);
        return new UserResponseModel(regUser);
        // vasko: response.sendRedirect("/home");
    }

    @PostMapping(value = "/login")
    public UserResponseModel loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                       HttpSession session) throws BadRequestException {
        User logUser = this.userService.login(userLoginFormDTO);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return new UserResponseModel(logUser);
        // vasko: response.sendRedirect("/home");
    }

    @PutMapping(value = "/change_password")
    public UserResponseModel changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                  HttpSession session) {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        User userAfterChangePassword = this.userService.changePassword(userChangePasswordDTO, user);
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, userAfterChangePassword);
        return new UserResponseModel(userAfterChangePassword);
        // vasko: response.sendRedirect("/profile");
    }

    @PostMapping(value = "/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "You are successful logout!";
        // vasko: response.sendRedirect("/home");
    }

    // todo this operation should only be performed by the Director of Sportal
    @DeleteMapping(value = "/remove/{" + USER_ID + "}")
    public UserResponseModel removeUser(@PathVariable(name = USER_ID) long userId,
                                        HttpSession session) throws BadRequestException {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new UserResponseModel(this.userService.adminRemoveUserByUserId(userId, user));
        // vasko: response.sendRedirect("/home");
    }
}
