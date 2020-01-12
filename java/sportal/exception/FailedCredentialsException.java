package sportal.exception;

public class FailedCredentialsException extends RuntimeException {

    public FailedCredentialsException(String massages) {
        super(massages);
    }
}
