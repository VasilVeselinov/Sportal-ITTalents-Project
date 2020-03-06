package sportal.model.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import sportal.model.service.dto.UserServiceDTO;

import java.sql.SQLException;

public interface IAuthService extends UserDetailsService {

    String FAILED_CREDENTIALS = "Validate your data is failed!";
    String NOT_EXISTS_OBJECT = "User not found!";

    UserServiceDTO registration(UserServiceDTO serviceDTO) throws SQLException;

    UserServiceDTO changePassword(UserServiceDTO serviceDTO, long userId);

    UserServiceDTO findUserByUsername(String username);
}
