package sportal.model.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import sportal.model.service.dto.UserServiceDTO;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private UserServiceDTO user;

    public OnRegistrationCompleteEvent(UserServiceDTO user) {
        super(user);
        this.user = user;
    }
}
