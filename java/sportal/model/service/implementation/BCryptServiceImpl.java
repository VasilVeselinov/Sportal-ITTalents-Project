package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sportal.model.service.IBCryptService;

@Service
public class BCryptServiceImpl implements IBCryptService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String cryptPassword(String text) {
        return this.passwordEncoder.encode(text);
    }

    @Override
    public boolean checkPassword(String text, String dbText) {
        return this.passwordEncoder.matches(text , dbText);
    }
}
