package sportal.controller;

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

    @GetMapping(value = "/registration_confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token) throws BadRequestException {
        this.emailService.confirmRegistration(token);
        return new ModelAndView("redirect:/users/login");
    }
}
