package sportal.model.data_validators;

import org.springframework.stereotype.Component;
import sportal.exception.FailedCredentialsException;
import sportal.exception.WrongCredentialsException;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserChangePasswordDTO;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static sportal.controller.AbstractController.WRONG_CREDENTIALS;

@Component
public class UserValidator {


    private static final String YOU_HAVE_EMPTY_FIELDS = "You have empty fields!";
    private static final String NOT_EQUAL_PASSWORD = "Your password must be the same as verification password!";
    private static final String EMAIL_IS_INVALID = "Your email is invalid!";
    private static final String NOT_STRONG_PASSWORD = "Your password is not strong!";
    private static final int MIN_NUMBER_OF_SYMBOLS = 8;
    private static final String SPECIAL_CHARACTER_PATTERN =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String INVALID_USER_NAME = "Your user name is invalid!";

    public UserRegistrationFormDTO checkForTheValidDataForRegistration(UserRegistrationFormDTO user) {
        if (user.getUserName() == null || user.getUserEmail() == null) {
            throw new FailedCredentialsException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (user.getUserPassword() == null || user.getVerificationPassword() == null) {
            throw new FailedCredentialsException(YOU_HAVE_EMPTY_FIELDS);
        }
        user.setUserName(user.getUserName().trim());
        if (user.getUserName().isEmpty() || user.getUserEmail().isEmpty()) {
            throw new FailedCredentialsException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (user.getUserPassword().isEmpty() || user.getVerificationPassword().isEmpty()){
            throw new FailedCredentialsException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (!user.getUserPassword().equals(user.getVerificationPassword())) {
            throw new FailedCredentialsException(NOT_EQUAL_PASSWORD);
        }
        if (!this.userNameCheck(user.getUserName())) {
            throw new FailedCredentialsException(INVALID_USER_NAME);
        }
        if (!this.emailValidation(user.getUserEmail())) {
            throw new FailedCredentialsException(EMAIL_IS_INVALID);
        }
        if (!user.getUserPassword().matches(SPECIAL_CHARACTER_PATTERN)) {
            throw new FailedCredentialsException(NOT_STRONG_PASSWORD);
        }
        return user;
    }

    private boolean userNameCheck(String userName) {
        return userName.length() < MIN_NUMBER_OF_SYMBOLS;
    }

    private boolean emailValidation(String userEmail) {
        if (userEmail == null) {
            return false;
        }
        try {
            InternetAddress emailAddr = new InternetAddress(userEmail);
            emailAddr.validate();
        } catch (AddressException e) {
            return false;
        }
        return true;
    }

    public boolean checkForTheValidDataForUpdate(UserChangePasswordDTO userChangePasswordDTO) {
        if (userChangePasswordDTO.getUserPassword().isEmpty()) {
            return false;
        }
        if (userChangePasswordDTO.getNewPassword().isEmpty() || userChangePasswordDTO.getVerificationPassword().isEmpty()) {
            return false;
        }
        if (!userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getVerificationPassword())) {
            return false;
        }
        if (!userChangePasswordDTO.getUserPassword().matches(SPECIAL_CHARACTER_PATTERN)) {
            return false;
        }
        return true;
    }

    public UserLoginFormDTO checkForTheValidDataForLogin(UserLoginFormDTO userLoginFormDTO) {
        if (userLoginFormDTO.getUserName() == null) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        if (userLoginFormDTO.getUserPassword() == null || userLoginFormDTO.getVerificationPassword() == null) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        userLoginFormDTO.setUserName(userLoginFormDTO.getUserName().trim());
        if (userLoginFormDTO.getUserName().isEmpty()) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        if (userLoginFormDTO.getUserPassword().isEmpty() || userLoginFormDTO.getVerificationPassword().isEmpty()) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        if (!userLoginFormDTO.getUserPassword().equals(userLoginFormDTO.getVerificationPassword())) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        return userLoginFormDTO;
    }
}
