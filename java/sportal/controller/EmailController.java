package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import sportal.exception.BadRequestException;
import sportal.model.service.IEmailService;

@RestController
@RequestMapping("/emails")
public class EmailController extends AbstractController {

    @Autowired
    private IEmailService emailService;
    private static final Logger LOGGER = LogManager.getLogger(EmailController.class);

    @GetMapping(value = "/registration_confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token) throws BadRequestException {
        LOGGER.info("GET /emails/registration_confirm");
        this.emailService.confirmRegistration(token);
        return new ModelAndView("redirect:/users/login");
    }
}
