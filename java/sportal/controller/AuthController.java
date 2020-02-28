package sportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sportal.controller.util.AuthValidator;
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
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class AuthController extends AbstractController {

    private static final String NOT_EQUAL_PASSWORD = "Your password must be the same as verification password!";

    @Autowired
    private IAuthService authService;

    @PostMapping(value = "/registration")
    public ModelAndView registerRequestBody(@Valid @RequestBody UserRegistrationModel userModel,
                                            BindingResult bindingResult) throws SQLException {
        if (!userModel.getPassword().equals(userModel.getVerificationPassword())) {
            throw new InvalidInputException(NOT_EQUAL_PASSWORD);
        }
        this.authService.registration(new UserServiceDTO(userModel));
        return new ModelAndView("redirect:/users/login");
    }

    @PutMapping(value = "/change_password")
    public UserResponseModel changePassword(@Valid @RequestBody UserChangePasswordModel userModel,
                                            BindingResult bindingResult, HttpSession session) {
        if (!userModel.getNewPassword().equals(userModel.getVerificationPassword())) {
            throw new InvalidInputException(NOT_EQUAL_PASSWORD);
        }
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        UserServiceDTO user = new UserServiceDTO(logUser.getId(), logUser.getUsername(), logUser.getUserEmail());
        return new UserResponseModel(this.authService.changePassword(new UserServiceDTO(userModel), user));
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login.html");
    }

    @GetMapping("/registration")
    public ModelAndView registerForm() {
        return new ModelAndView("register.html");
    }
}
