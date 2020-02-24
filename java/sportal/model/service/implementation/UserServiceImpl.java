package sportal.model.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sportal.exception.AuthorizationException;
import sportal.exception.ExistsObjectException;
import sportal.model.validators.UserValidator;
import sportal.model.db.pojo.User;
import sportal.model.db.repository.UserRepository;
import sportal.model.service.IUserService;
import sportal.model.service.dto.UserServiceDTO;

import java.util.Optional;

import static sportal.model.validators.AbstractValidator.*;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserServiceDTO adminRemoveUserByUserId(long userId, UserServiceDTO user) {
        UserServiceDTO logUser = UserValidator.checkUserIsLogged(user);
        UserValidator.checkUserIsAdmin(logUser);
        Optional<User> existsUser = this.userRepository.findById(userId);
        if (!existsUser.isPresent()) {
            throw new ExistsObjectException(NOT_EXISTS_OBJECT);
        }
        if (logUser.getId() == userId) {
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
        this.userRepository.deleteById(userId);
        return new UserServiceDTO(existsUser.get());
    }
}
