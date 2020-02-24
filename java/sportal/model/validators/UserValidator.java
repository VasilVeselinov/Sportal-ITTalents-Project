package sportal.model.validators;

import sportal.exception.AuthorizationException;
import sportal.exception.BadRequestException;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.UserServiceDTO;
import sportal.model.service.implementation.BCryptServiceImpl;

public class UserValidator extends AbstractValidator {

    public static UserServiceDTO checkCredentials(UserServiceDTO user, UserServiceDTO serviceDTO) {
        if (!serviceDTO.getNewPassword().equals(serviceDTO.getVerificationPassword())) {
            throw new AuthorizationException(NOT_EQUAL_PASSWORD);
        }
        if (!BCryptServiceImpl.checkPassword(serviceDTO.getUserPassword(), user.getUserPassword())) {
            throw new AuthorizationException(FAILED_CREDENTIALS);
        }
        return user;
    }

    public static UserServiceDTO checkForTheValidDataForRegistration(UserServiceDTO serviceDTO) {
        if (!serviceDTO.getUserPassword().equals(serviceDTO.getVerificationPassword())) {
            throw new AuthorizationException(NOT_EQUAL_PASSWORD);
        }
        return serviceDTO;
    }

    public static UserServiceDTO checkForTheValidDataForLogin(UserServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getUsername() == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (serviceDTO.getUserPassword() == null || serviceDTO.getVerificationPassword() == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (serviceDTO.getUsername().isEmpty()) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (serviceDTO.getUserPassword().isEmpty() || serviceDTO.getVerificationPassword().isEmpty()) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (!serviceDTO.getUserPassword().equals(serviceDTO.getVerificationPassword())) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        return serviceDTO;
    }

    public static UserServiceDTO checkCredentialsOfUserFromDB(User user, UserServiceDTO serviceDTO) {
        if (user == null) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        if (!BCryptServiceImpl.checkPassword(serviceDTO.getUserPassword(), user.getPassword())) {
            throw new AuthorizationException(WRONG_CREDENTIALS);
        }
        serviceDTO.setId(user.getId());
        serviceDTO.setUserEmail(user.getUserEmail());
        serviceDTO.setIsAdmin(user.getIsAdmin());
        return serviceDTO;
    }

    public static UserServiceDTO checkUserIsLogged(UserServiceDTO user) {
        if (user == null) {
            throw new AuthorizationException(LOGIN_MESSAGES);
        }
        return user;
    }

    public static void checkUserIsAdmin(UserServiceDTO user) {
        if (!user.getIsAdmin()) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }
}
