package sportal.model.service;

import sportal.exception.BadRequestException;
import sportal.controller.models.user.UserChangePasswordDTO;
import sportal.controller.models.user.UserLoginFormDTO;
import sportal.controller.models.user.UserRegistrationFormDTO;
import sportal.model.db.pojo.User;
import sportal.model.service.dto.UserServiceDTO;

public interface IUserService {
    UserServiceDTO registration(UserRegistrationFormDTO userRegFormDTO) throws BadRequestException;

    User login(UserLoginFormDTO userLoginFormDTO) throws BadRequestException;

    User changePassword(UserChangePasswordDTO userChangePasswordDTO, User user);

    UserServiceDTO adminRemoveUserByUserId(long userId, User user) throws BadRequestException;
}
