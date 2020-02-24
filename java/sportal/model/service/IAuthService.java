package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.UserServiceDTO;

public interface IAuthService {

    UserServiceDTO registration(UserServiceDTO serviceDTO) throws BadRequestException;

    UserServiceDTO login(UserServiceDTO serviceDTO) throws BadRequestException;

    UserServiceDTO changePassword(UserServiceDTO serviceDTO, UserServiceDTO userOfSession);
}
