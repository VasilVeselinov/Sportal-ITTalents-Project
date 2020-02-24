package sportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import sportal.exception.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Validated
public abstract class AbstractController {

    // key session
    static final String LOGGED_USER_KEY_IN_SESSION = "loggedUser";

    // responses
    private static final String WRONG_REQUEST = "Invalid request!";
    private static final String SOMETHING_WENT_WRONG = "Please contact IT team!";
    static final String MASSAGE_FOR_INVALID_ID = "Id must be greater than 0!";

    // parameters
    static final String USER_ID = "user_id";
    static final String ARTICLE_ID = "article_id";
    static final String CATEGORY_ID = "category_id";
    static final String COMMENT_ID = "comment_id";
    static final String PICTURE_ID = "picture_id";
    static final String LOCATION = "Location";

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionObject handlerOfAuthorizationException(Exception e) {
        return new ExceptionObject(
                e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler({BadRequestException.class, ExistsObjectException.class, InvalidInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfBadRequestException(Exception e) {
        return new ExceptionObject(
                e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfConstraintViolationException(ConstraintViolationException e) {
        String message = "";
        for (ConstraintViolation<?> eex : e.getConstraintViolations()) {
            message= eex.getMessage();
        }
        return new ExceptionObject(
                message, HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfJsonParseException(Exception e) {
        return new ExceptionObject(
                WRONG_REQUEST, HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionObject handlerOfHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return new ExceptionObject(
                WRONG_REQUEST, HttpStatus.METHOD_NOT_ALLOWED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionObject handlerOfMethodArgumentTypeMismatchException(Exception e) {
        return new ExceptionObject(
                WRONG_REQUEST, HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ExceptionObject handlerOfHttpMediaTypeNotSupportedExceptionException(Exception e) {
        return new ExceptionObject(
                WRONG_REQUEST, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
    }

    @ExceptionHandler({TransactionException.class, IOException.class, SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionObject handlerOfTransactionIOAndSQLException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                SOMETHING_WENT_WRONG,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        System.out.println(e.getMessage());
        System.out.println(LocalDateTime.now());
        return exceptionObject;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)  // vasko:
    public ExceptionObject handlerOfException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                SOMETHING_WENT_WRONG,
                HttpStatus.I_AM_A_TEAPOT.value(),
                LocalDateTime.now(),
                e.getClass().getName()
        );
        System.out.println(e.getMessage());
        System.out.println(LocalDateTime.now());
        return exceptionObject;
    }
}
