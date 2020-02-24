package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.model.service.dto.UserServiceDTO;

public interface IUserService {

    UserServiceDTO adminRemoveUserByUserId(long userId, UserServiceDTO userOfSession) throws BadRequestException;
}
