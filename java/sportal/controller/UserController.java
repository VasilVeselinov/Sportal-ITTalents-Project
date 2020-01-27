package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sportal.exception.*;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserResponseDTO;
import sportal.model.dto.user.UserChangePasswordDTO;
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
        UserResponseDTO userResponseDTO = this.userService.registration(userRegFormDTO, session);
        return userResponseDTO;
    }

    @PostMapping(value = "/login")
    public UserResponseDTO loginUser(@RequestBody UserLoginFormDTO userLoginFormDTO,
                                     HttpSession session) throws BadRequestException {
        UserResponseDTO userResponseDTO = this.userService.login(userLoginFormDTO, session);
        return userResponseDTO;
    }

    @PutMapping(value = "/change_password")
    public UserResponseDTO changePasswordOfUser(@RequestBody UserChangePasswordDTO userChangePasswordDTO,
                                                HttpSession session) {
        UserResponseDTO userResponseDTO = this.userService.changePassword(userChangePasswordDTO, session);
        return userResponseDTO;
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
        UserResponseDTO userResponseDTO = this.userService.adminRemoveUserByUserId(userId, session);
        return userResponseDTO;
    }
}
