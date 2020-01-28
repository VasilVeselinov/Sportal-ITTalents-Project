package sportal.model.data_validators;

import org.springframework.security.crypto.bcrypt.BCrypt;

class BCryptValidator {

    private static final int salt = 7;

    static String cryptPassword(String userPassword) {
        String salt = BCrypt.gensalt(BCryptValidator.salt);
        String cryptPassword = BCrypt.hashpw(userPassword, salt);
        return (cryptPassword);
    }

    static boolean checkPassword(String userPassword, String dbUserPassword) {
        return BCrypt.checkpw(userPassword, dbUserPassword);
    }
}
