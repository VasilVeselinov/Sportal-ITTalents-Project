package sportal.model.data_validators;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptValidator {

    private static final int salt = 7;

    public static String cryptPassword(String dbUserPassword) {
        String salt = BCrypt.gensalt(BCryptValidator.salt);
        String cryptPassword = BCrypt.hashpw(dbUserPassword, salt);
        return (cryptPassword);
    }

    public static boolean checkPassword(String userPassword, String dbUserPassword) {
        return BCrypt.checkpw(userPassword, dbUserPassword);
    }
}
