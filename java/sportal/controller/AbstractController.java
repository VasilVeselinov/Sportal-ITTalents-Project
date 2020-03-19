package sportal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;
import sportal.exception.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.apache.logging.log4j.Level.INFO;

@Validated
@RestControllerAdvice
public abstract class AbstractController {

    // key session
    public static final String LOGGED_USER_KEY_IN_SESSION = "loggedUser";

    // responses
    private static final String SOMETHING_WENT_WRONG = "Please contact IT team!";
    private static final String SOMETHING_WENT_WRONG_ = "Please contact IT team!";
    static final String MASSAGE_FOR_INVALID_ID = "Id must be greater than 0!";
    static final String MASSAGE_FOR_INVALID_NUMBER_OF_PAGE = "Invalid number of page!";
    static final String WITHOUT_FILE_MASSAGE = "No attached file!";
    private static final String WRONG_REQUEST = "Invalid request!";

    // parameters
    static final String USER_ID = "user_id";
    static final String ARTICLE_ID = "article_id";
    static final String CATEGORY_ID = "category_id";
    static final String COMMENT_ID = "comment_id";
    static final String PICTURE_ID = "picture_id";
    static final String VIDEO_ID = "video_id";
    static final String LOCATION = "Location";

    private final Logger logger = LogManager.getLogger(AbstractController.class);

    @ExceptionHandler({AuthorizationException.class, AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView handlerOfAuthException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.UNAUTHORIZED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("unauthorized.html");
        view.addObject("error", e.getMessage());
        return view;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfBadRequestException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", e.getMessage());
        return view;
    }

    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ModelAndView handlerOfInvalidInputException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.CONFLICT.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", e.getMessage());
        return view;
    }

    @ExceptionHandler(NotExistsObjectException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handlerOfNotExistsObjectException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", e.getMessage());
        return view;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> eex : e.getConstraintViolations()) {
            message.append(eex.getMessage());
        }
        ExceptionObject exceptionObject = new ExceptionObject(
                message.toString(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", message.toString());
        return view;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handlerOfJsonParseException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", WRONG_REQUEST);
        return view;
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, RequestRejectedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView handlerOfHttpRequestMethodNotSupportedException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", WRONG_REQUEST);
        return view;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ModelAndView handlerOfHttpMediaTypeNotSupportedExceptionException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.log(INFO, exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", WRONG_REQUEST);
        return view;
    }

    @ExceptionHandler({TransactionException.class, IOException.class, SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handlerOfTransactionIOAndSQLException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.error(exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", SOMETHING_WENT_WRONG);
        return view;
    }

    @ExceptionHandler({NoHandlerFoundException.class, TemplateInputException.class})
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ModelAndView handlerOfNoHandlerFoundException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.BAD_GATEWAY.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.error(exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", SOMETHING_WENT_WRONG);
        return view;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ModelAndView handlerOfException(Exception e) {
        ExceptionObject exceptionObject = new ExceptionObject(
                e.getMessage(), HttpStatus.I_AM_A_TEAPOT.value(),
                LocalDateTime.now(), e.getClass().getName()
        );
        this.logger.error(exceptionObject);
        ModelAndView view = new ModelAndView("error.html");
        view.addObject("error", SOMETHING_WENT_WRONG);
        return view;
    }
}
