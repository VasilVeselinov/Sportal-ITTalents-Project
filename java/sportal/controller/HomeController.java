package sportal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView index(Authentication authentication) {
        if(authentication != null && authentication.isAuthenticated()) {
            return new ModelAndView("home.html");
        }
        return new ModelAndView("index.html");
    }

    @GetMapping("/about")
    public ModelAndView about() {
        return new ModelAndView("about.html");
    }

    @GetMapping("/unauthorized")
    public ModelAndView unauthorized() {
        return new ModelAndView("unauthorized.html");
    }
}
