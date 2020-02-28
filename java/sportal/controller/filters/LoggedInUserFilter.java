package sportal.controller.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sportal.controller.models.user.UserLoginModel;
import sportal.controller.util.IAuthenticationFacade;
import sportal.model.service.IAuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static sportal.controller.AbstractController.LOGGED_USER_KEY_IN_SESSION;

@Component
public class LoggedInUserFilter implements Filter {

    @Autowired
    private IAuthenticationFacade authenticatedUserService;
    @Autowired
    private IAuthService authService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String username = this.authenticatedUserService.getUsername();
        if (username.equals("anonymousUser")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpSession session = ((HttpServletRequest) servletRequest).getSession();
        UserLoginModel loginModel = (UserLoginModel) session.getAttribute(LOGGED_USER_KEY_IN_SESSION);
        if (loginModel == null){
            loginModel = new UserLoginModel(this.authService.findUserByUsername(username));
            session.setAttribute(LOGGED_USER_KEY_IN_SESSION, loginModel);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
