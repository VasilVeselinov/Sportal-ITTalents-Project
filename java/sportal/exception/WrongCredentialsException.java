package sportal.exception;

public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException(String messages){
        super(messages);
    }
}
