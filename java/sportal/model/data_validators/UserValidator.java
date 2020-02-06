package sportal.model.data_validators;

import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.model.dto.user.UserLoginFormDTO;
import sportal.model.dto.user.UserRegistrationFormDTO;
import sportal.model.dto.user.UserChangePasswordDTO;
import sportal.model.pojo.User;

public class UserValidator extends AbstractValidator {

    public static User checkCredentials(User user, UserChangePasswordDTO userChangePasswordDTO) {
        if (userChangePasswordDTO.getUserPassword() == null || userChangePasswordDTO.getUserPassword().isEmpty()) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (userChangePasswordDTO.getNewPassword() == null || userChangePasswordDTO.getNewPassword().isEmpty()) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (userChangePasswordDTO.getVerificationPassword() == null ||
                userChangePasswordDTO.getVerificationPassword().isEmpty()) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (!userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getVerificationPassword())) {
            throw new AuthorizationException(NOT_EQUAL_PASSWORD);
        }
        if (!userChangePasswordDTO.getNewPassword().matches(SPECIAL_CHARACTER_PATTERN_FOR_PASSWORD)) {
            throw new AuthorizationException(NOT_STRONG_PASSWORD);
        }
        if (!BCryptValidator.checkPassword(userChangePasswordDTO.getUserPassword(), user.getUserPassword())) {
            throw new AuthorizationException(FAILED_CREDENTIALS);
        }
        String cryptPassword = BCryptValidator.cryptPassword(userChangePasswordDTO.getNewPassword());
        user.setUserPassword(cryptPassword);
        return user;
    }

    public static UserRegistrationFormDTO checkForTheValidDataForRegistration(UserRegistrationFormDTO user) {
        if (user.getUserName() == null || user.getUserEmail() == null) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (user.getUserPassword() == null || user.getVerificationPassword() == null) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        user.setUserName(user.getUserName().trim());
        if (user.getUserName().isEmpty() || user.getUserEmail().isEmpty()) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (user.getUserPassword().isEmpty() || user.getVerificationPassword().isEmpty()) {
            throw new AuthorizationException(YOU_HAVE_EMPTY_FIELDS);
        }
        if (!user.getUserPassword().equals(user.getVerificationPassword())) {
            throw new AuthorizationException(NOT_EQUAL_PASSWORD);
        }
        if (!userNameCheck(user.getUserName())) {
            throw new AuthorizationException(INVALID_USER_NAME);
        }
        if (!emailValidation(user.getUserEmail())) {
            throw new AuthorizationException(EMAIL_IS_INVALID);
        }
        if (!user.getUserPassword().matches(SPECIAL_CHARACTER_PATTERN_FOR_PASSWORD)) {
            throw new AuthorizationException(NOT_STRONG_PASSWORD);
        }
        String cryptPassword = BCryptValidator.cryptPassword(user.getUserPassword());
        user.setUserPassword(cryptPassword);
        return user;
    }

    private static boolean userNameCheck(String userName) {
        return userName.length() > MIN_NUMBER_OF_SYMBOLS_FOR_USER_NAME &&
                userName.length() < MAX_NUMBER_OF_SYMBOLS_FOR_USER_NAME;
    }

    private static boolean emailValidation(String userEmail) {
        if (userEmail == null) {
            return false;
        }
        if (userEmail.length() > MAX_NUMBER_OF_SYMBOL_FOR_EMAIL ||
                userEmail.length() < MIN_NUMBER_OF_SYMBOL_FOR_EMAIL) {
            throw new AuthorizationException(VALID_EMAIL);
        }
        return userEmail.matches(SPECIAL_CHARACTER_PATTERN_FOR_EMAIL);
    }

    public static UserLoginFormDTO checkForTheValidDataForLogin(
            UserLoginFormDTO userLoginFormDTO) throws BadRequestException {
        if (userLoginFormDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (userLoginFormDTO.getUserName() == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (userLoginFormDTO.getUserPassword() == null || userLoginFormDTO.getVerificationPassword() == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        userLoginFormDTO.setUserName(userLoginFormDTO.getUserName().trim());
        if (userLoginFormDTO.getUserName().isEmpty()) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (userLoginFormDTO.getUserPassword().isEmpty() || userLoginFormDTO.getVerificationPassword().isEmpty()) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (!userLoginFormDTO.getUserPassword().equals(userLoginFormDTO.getVerificationPassword())) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        return userLoginFormDTO;
    }

    public static User checkCredentialsOfUserFromDB(User user, UserLoginFormDTO validLogUser) {
        if (user == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (!BCryptValidator.checkPassword(validLogUser.getUserPassword(), user.getUserPassword())) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        return user;
    }

    public static User checkUserIsLogged(User user) {
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        return user;
    }

    public static void checkUserIsAdmin(User user) {
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }
}
