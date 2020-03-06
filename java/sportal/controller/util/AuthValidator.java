package sportal.controller.util;

import sportal.controller.models.user.UserLoginModel;
import sportal.exception.AuthorizationException;
import sportal.model.service.dto.RoleServiceDTO;

import static sportal.GlobalConstants.ADMIN_USER_AUTHORITY;
import static sportal.GlobalConstants.EDITOR_USER_AUTHORITY;

public class AuthValidator {

    private static final String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed!";

    public static void checkUserIsAdmin(UserLoginModel user) {
        boolean hasAdmin = false;
        for (RoleServiceDTO role : user.getAuthorities()) {
            if (role.getAuthority().equals(ADMIN_USER_AUTHORITY)) {
                hasAdmin = true;
                break;
            }
        }
        if (!hasAdmin){
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }

    public static void checkUserIsEditor(UserLoginModel user) {
        boolean hasEditor = false;
        for (RoleServiceDTO role : user.getAuthorities()) {
            if (role.getAuthority().equals(EDITOR_USER_AUTHORITY)) {
                hasEditor = true;
                break;
            }
        }
        if (!hasEditor){
            throw new AuthorizationException(NOT_ALLOWED_OPERATION);
        }
    }
}
