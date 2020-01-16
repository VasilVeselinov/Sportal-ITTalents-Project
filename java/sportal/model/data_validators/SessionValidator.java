package sportal.model.data_validators;

import sportal.exception.AuthorizationException;
import sportal.model.pojo.User;

import javax.servlet.http.HttpSession;

import static sportal.controller.AbstractController.*;

public class SessionValidator extends AbstractValidator {

   private static final String LOGIN_MESSAGES = "You must to log in!";

    public static User checkUserIsLogged(HttpSession session) {
        User user = (User) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
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
