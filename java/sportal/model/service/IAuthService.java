package sportal.model.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;

public interface IAuthService extends UserDetailsService {

    String FAILED_CREDENTIALS = "Validate your data is failed!";
    String NOT_EXISTS_OBJECT = "User not found!";

    void registration(UserServiceDTO serviceDTO) throws SQLException;

    UserServiceDTO changePassword(UserServiceDTO serviceDTO, UserServiceDTO userOfSession);

    UserServiceDTO findUserByUsername(String username);
}
