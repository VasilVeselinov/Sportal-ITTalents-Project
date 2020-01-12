package sportal.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String messages){
        super(messages);
    }
}
