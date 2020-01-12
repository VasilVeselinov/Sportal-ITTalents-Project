package sportal.exception;

public class SomethingWentWrongException extends RuntimeException {

    public SomethingWentWrongException(String messages){
        super(messages);
    }
}
