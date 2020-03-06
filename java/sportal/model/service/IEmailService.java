package sportal.model.service;

import sportal.exception.BadRequestException;

public interface IEmailService{

    void sendEmail(String to, String subject, String text);

    void confirmRegistration(String token) throws BadRequestException;
}
