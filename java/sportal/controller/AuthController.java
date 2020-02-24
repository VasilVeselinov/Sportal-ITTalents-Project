package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sportal.controller.models.user.UserLoginModel;
import sportal.exception.*;
import sportal.controller.models.user.UserRegistrationModel;
import sportal.controller.models.user.UserResponseModel;
import sportal.controller.models.user.UserChangePasswordModel;
import sportal.model.service.IAuthService;
import sportal.model.service.IUserService;
import sportal.model.service.dto.UserServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/users")
public class AuthController extends AbstractController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAuthService authService;


    @PostMapping(value = "/registration")
    public UserResponseModel registration(@Valid @RequestBody UserRegistrationModel userModel,
                                          BindingResult bindingResult) throws BadRequestException {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult.getFieldError().getDefaultMessage());
        }
        UserServiceDTO regUser = this.authService.registration(new UserServiceDTO(userModel));
        return new UserResponseModel(regUser);
    }

    @PostMapping(value = "/login")
    public UserResponseModel login(@Valid @RequestBody UserLoginModel userLoginModel,
                                   HttpSession session) throws BadRequestException {
        UserResponseModel logUser = new UserResponseModel(this.authService.login(new UserServiceDTO(userLoginModel)));
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return logUser;
    }

    @PutMapping(value = "/change_password")
    public UserResponseModel changePassword(@Valid @RequestBody UserChangePasswordModel userChangePasswordModel,
                                            BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult.getFieldError().getDefaultMessage());
        }
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        UserResponseModel logUser = new UserResponseModel(
                this.authService.changePassword(new UserServiceDTO(userChangePasswordModel), user)
        );
        session.setAttribute(LOGGED_USER_KEY_IN_SESSION, logUser);
        return logUser;
    }

    @PostMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "You are successful logout!";
    }

    // todo this operation should only be performed by the Director of Sportal
    @DeleteMapping(value = "/remove/{" + USER_ID + "}")
    public UserResponseModel removeUser(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) throws BadRequestException {
        UserResponseModel userOfSession = (UserResponseModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(
                userOfSession.getId(),
                userOfSession.getUsername(),
                userOfSession.getUserEmail(),
                userOfSession.getIsAdmin());
        return new UserResponseModel(this.userService.adminRemoveUserByUserId(userId, user));
    }
}
