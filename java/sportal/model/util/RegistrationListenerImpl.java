package sportal.model.util;

import org.springframework.beans.factory.annotation.Autowired;
import sportal.annotations.Listener;
import sportal.model.service.IEmailService;

@Listener
public class RegistrationListenerImpl implements IRegistrationListener {

    @Autowired
    private IEmailService emailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        String recipientAddress =  event.getUser().getUserEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "/emails/registration_confirm?token=" + event.getUser().getToken();
        String message = "Please click for confirm your registration:";
        String text = message + "\r\n" + "http://localhost:8888" + confirmationUrl;
        this.emailService.sendEmail(recipientAddress, subject, text);
    }
}
