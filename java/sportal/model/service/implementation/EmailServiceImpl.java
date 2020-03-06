package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sportal.exception.BadRequestException;
import sportal.model.service.IEmailService;
import sportal.model.service.IUserService;

@Service
public class EmailServiceImpl implements IEmailService {

    private class Sender extends Thread {

        private SimpleMailMessage email;
        private JavaMailSender emailSender;

        Sender(JavaMailSender emailSender, SimpleMailMessage email) {
            this.email = email;
            this.emailSender = emailSender;
        }

        @Override
        public void run() {
            this.emailSender.send(this.email);
        }
    }

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private IUserService userService;


    @Override
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);
        Sender sender = new Sender(this.emailSender, email);
        sender.start();
    }

    @Override
    public void confirmRegistration(String token) throws BadRequestException {
        this.userService.confirmToken(token);
    }
}
