package sportal.controller.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sportal.controller.AbstractController;
import sportal.controller.models.user.UserRegistrationModel;
import sportal.exception.BadRequestException;
import sportal.model.service.IAuthService;
import sportal.model.service.dto.UserServiceDTO;

import javax.validation.Valid;
import java.sql.SQLException;

//@Controller
//@RequestMapping("/users")
public class AuthControllerV extends AbstractController {

//    @ModelAttribute("registrationModel")
//    public UserRegistrationModel registrationModel() {
//        return new UserRegistrationModel();
//    }

//
//    @Autowired
//    private IAuthService authService;
//
//
//    @GetMapping("/login")
//    public ModelAndView login() {
//        return new ModelAndView("login.html");
//    }
//
//    @GetMapping("/registration")
//    public String registerForm(@ModelAttribute("registrationModel") UserRegistrationModel userModel,
//                                     ModelAndView view) {
//        return "register.html";
//    }

//    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public String registration(@Valid @ModelAttribute("registrationModel") UserRegistrationModel userModel,
//                                     BindingResult bindingResult,
//                                     ModelAndView view) throws SQLException {
//        if(bindingResult.hasErrors()){
//            view.setViewName("redirect:/users/registration");
//            view.addObject("registrationModel", userModel);
//            return "register.html";
//        }
//
//        if (!userModel.getPassword().equals(userModel.getVerificationPassword())) {
//            return "register.html";
//        }
//        System.out.println(userModel.toString());
//        UserServiceDTO regUser = this.authService.registration(new UserServiceDTO(userModel));
//        return "redirect:/users/login";
//    }
}
