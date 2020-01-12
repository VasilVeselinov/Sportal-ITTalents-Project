package sportal.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String messages){
        super(messages);
    }
}
