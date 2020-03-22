package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController extends AbstractController {

    private static final Logger LOGGER = LogManager.getLogger(HomeController.class);

    @GetMapping("/")
    public ModelAndView index(Authentication authentication) {
        LOGGER.info("GET /");
        if (authentication != null && authentication.isAuthenticated()) {
            return new ModelAndView("home.html");
        }
        return new ModelAndView("index.html");
    }

    @GetMapping("/unauthorized")
    public ModelAndView unauthorized() {
        LOGGER.info("GET /unauthorized");
        return new ModelAndView("unauthorized.html");
    }

    @GetMapping("/after_registration")
    public ModelAndView afterRegistration() {
        LOGGER.info("GET /after_registration");
        return new ModelAndView("after_registration.html");
    }

    @GetMapping("/favicon.ico")
    public void favicon() {
        LOGGER.info("GET /favicon.ico");
    }
}
