package sportal.controller.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import sportal.annotations.Facade;

@Facade
public class AuthenticationFacade implements IAuthenticationFacade {

    @Override
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
