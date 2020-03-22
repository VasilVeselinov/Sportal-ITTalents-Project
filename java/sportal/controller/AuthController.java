package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sportal.controller.models.user.UserLoginModel;
import sportal.exception.*;
import sportal.controller.models.user.UserRegistrationModel;
import sportal.controller.models.user.UserResponseModel;
import sportal.controller.models.user.UserChangePasswordModel;
import sportal.model.service.IAuthService;
import sportal.model.service.dto.UserServiceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.sql.SQLException;

import static sportal.util.GlobalConstants.HAS_AUTHORITY_EDITOR;

@RestController
@RequestMapping("/users")
public class AuthController extends AbstractController {

    private static final String NOT_EQUAL_PASSWORD = "Your password must be the same as verification password!";

    @Autowired
    private IAuthService authService;
    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @PostMapping(value = "/registration")
    public ModelAndView registrationOfUser(@Valid @RequestBody UserRegistrationModel userModel,
                                            BindingResult bindingResult) throws SQLException {
        LOGGER.info("POST /users/registration");
        if (!userModel.getPassword().equals(userModel.getVerificationPassword())) {
            throw new InvalidInputException(NOT_EQUAL_PASSWORD);
        }
        this.authService.registration(
                new UserServiceDTO(userModel.getUsername(), userModel.getUserEmail(), userModel.getPassword()));
        return new ModelAndView("redirect:/after_registration");
    }

    @PutMapping(value = "/change_password")
    public UserResponseModel changePassword(@Valid @RequestBody UserChangePasswordModel userModel,
                                            BindingResult bindingResult, HttpSession session) {
        LOGGER.info("PUT /users/change_password");
        if (!userModel.getNewPassword().equals(userModel.getVerificationPassword())) {
            throw new InvalidInputException(NOT_EQUAL_PASSWORD);
        }
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        return new UserResponseModel(this.authService.changePassword(
                new UserServiceDTO(userModel.getUserPassword(), userModel.getNewPassword()), logUser.getId()));
    }

    @PutMapping(value = "/up_authority/{" + USER_ID + "}")
    @PreAuthorize(HAS_AUTHORITY_EDITOR)
    public ResponseEntity<Void> authoriseUsersById(
            @PathVariable(name = USER_ID) @Positive(message = MASSAGE_FOR_INVALID_ID) long userId,
            HttpSession session) throws BadRequestException {
        LOGGER.info("PUT /users/up_authority/{" + USER_ID + "}");
        UserLoginModel logUser = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        this.authService.authorise(userId, logUser.getAuthorities());
        HttpHeaders headers = new HttpHeaders();
        headers.add(LOCATION, "/users/" + userId);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        LOGGER.info("GET /users/login");
        return new ModelAndView("login.html");
    }

    @GetMapping("/registration")
    public ModelAndView registrationForm() {
        LOGGER.info("GET /users/registration");
        return new ModelAndView("register.html");
    }
}
