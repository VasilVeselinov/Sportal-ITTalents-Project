package sportal.exception;

import java.sql.SQLException;

public class TransactionException extends SQLException {

    public TransactionException(String messages){
        super(messages);
    }
}
