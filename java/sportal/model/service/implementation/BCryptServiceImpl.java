package sportal.model.service.implementation;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import sportal.model.service.IBCryptService;

@Service
public class BCryptServiceImpl implements IBCryptService {

    private static final int salt = 7;

    @Override
    public String cryptPassword(String userPassword) {
        String salt = BCrypt.gensalt(BCryptServiceImpl.salt);
        String cryptPassword = BCrypt.hashpw(userPassword, salt);
        return (cryptPassword);
    }

    public static boolean checkPassword(String userPassword, String dbUserPassword) {
        return BCrypt.checkpw(userPassword, dbUserPassword);
    }
}
