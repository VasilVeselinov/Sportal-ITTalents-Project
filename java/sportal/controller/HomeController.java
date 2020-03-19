package sportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends AbstractController {

    @GetMapping("/")
    public ModelAndView index(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return new ModelAndView("home.html");
        }
        return new ModelAndView("index.html");
    }

    @GetMapping("/unauthorized")
    public ModelAndView unauthorized() {
        return new ModelAndView("unauthorized.html");
    }

    @GetMapping("/after_registration")
    public ModelAndView afterRegistration() {
        return new ModelAndView("after_registration.html");
    }

    @GetMapping("/favicon.ico")
    public ModelAndView favicon() {
        return new ModelAndView("home.html");
    }
}
