package sportal.model.data_validators;

import sportal.exception.FailedCredentialsException;
import sportal.exception.WrongCredentialsException;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.pojo.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class UserValidator extends AbstractValidator {


    private static final String YOU_HAVE_EMPTY_FIELDS = "You have empty fields!";
    private static final String NOT_EQUAL_PASSWORD = "Your password must be the same as verification password!";
    private static final String EMAIL_IS_INVALID = "Your email is invalid!";
    private static final String NOT_STRONG_PASSWORD = "Your password is not strong!";
    private static final int MIN_NUMBER_OF_SYMBOLS = 8;
    private static final String SPECIAL_CHARACTER_PATTERN =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
    private static final String INVALID_USER_NAME = "Your user name is invalid!";
    private static final String FAILED_CREDENTIALS = "Validate your data is failed!";
    private static final String WRONG_CREDENTIALS = "Your username or password is wrong!";

    public static User checkCredentials(User user, UserChangePasswordDTO userChangePasswordDTO ) {
        if (userChangePasswordDTO.getUserPassword().isEmpty()) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        if (userChangePasswordDTO.getNewPassword().isEmpty() || userChangePasswordDTO.getVerificationPassword().isEmpty()) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        if (!userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getVerificationPassword())) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        if (!userChangePasswordDTO.getUserPassword().matches(SPECIAL_CHARACTER_PATTERN)) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }
        if (!BCryptValidator.checkPassword(userChangePasswordDTO.getUserPassword(), user.getUserPassword())) {
            throw new FailedCredentialsException(FAILED_CREDENTIALS);
        }

        user.setUserPassword(userChangePasswordDTO.getNewPassword());
        return user;
    }

    public static UserRegistrationFormDTO checkForTheValidDataForRegistration(UserRegistrationFormDTO user) {
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
        if (!userNameCheck(user.getUserName())) {
            throw new FailedCredentialsException(INVALID_USER_NAME);
        }
        if (!emailValidation(user.getUserEmail())) {
            throw new FailedCredentialsException(EMAIL_IS_INVALID);
        }
        if (!user.getUserPassword().matches(SPECIAL_CHARACTER_PATTERN)) {
            throw new FailedCredentialsException(NOT_STRONG_PASSWORD);
        }
        return user;
    }

    private static boolean userNameCheck(String userName) {
        return userName.length() > MIN_NUMBER_OF_SYMBOLS;
    }

    private static boolean emailValidation(String userEmail) {
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

    public static UserLoginFormDTO checkForTheValidDataForLogin(UserLoginFormDTO userLoginFormDTO) {
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

    public static User checkCredentialsOfUserFromDB(User user, UserLoginFormDTO validLogUser) {
        if (user == null) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        if (!BCryptValidator.checkPassword(validLogUser.getUserPassword(), user.getUserPassword())) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS);
        }
        return user;
    }
}
