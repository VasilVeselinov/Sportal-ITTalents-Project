package sportal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.exceptions.TemplateInputException;
import sportal.exception.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Validated
public abstract class AbstractController {

    // key session
    public static final String LOGGED_USER_KEY_IN_SESSION = "loggedUser";

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

    @ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handlerOfAuthorizationException(Exception e) {
        ModelAndView view = new ModelAndView("unauthorized.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler({BadRequestException.class, ExistsObjectException.class, InvalidInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfBadRequestException(Exception e) {
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder("");
        for (ConstraintViolation<?> eex : e.getConstraintViolations()) {
            message.append(eex.getMessage());
        }
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                message.toString(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfJsonParseException(Exception e) {
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                WRONG_REQUEST, HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handlerOfHttpRequestMethodNotSupportedException(Exception e) {
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                WRONG_REQUEST, HttpStatus.METHOD_NOT_ALLOWED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ModelAndView handlerOfHttpMediaTypeNotSupportedExceptionException(Exception e) {
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                WRONG_REQUEST, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler({TransactionException.class, IOException.class, SQLException.class, TemplateInputException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerOfTransactionIOAndSQLException(Exception e) {
        System.out.println(e.getMessage());
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)  // vasko:
    public ModelAndView handlerOfException(Exception e) {
        System.out.println(e.getMessage());
        ModelAndView view = new ModelAndView("error.html");
        ExceptionObject exceptionObject = new ExceptionObject(
                SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        System.out.println(exceptionObject);
        view.addObject("ExceptionObject", exceptionObject);
        view.addObject("message", exceptionObject.getMessages());
        return view;
    }
}
