package sportal.model.data_validators;

public class AbstractValidator {

    static final String YOU_HAVE_EMPTY_FIELDS = "You have empty fields!";
    static final int MIN_NUMBER_OF_SYMBOL_FOR_EMAIL = 8;
    static final int MAX_NUMBER_OF_SYMBOL_FOR_EMAIL = 40;
    static final String VALID_EMAIL = "Your email have to max 40 symbols! ";
    static final String SPECIAL_CHARACTER_PATTERN_FOR_EMAIL = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    static final String EMAIL_IS_INVALID = "Your email is invalid! " +
            "Example for valid email: email@hostexpansion.countryexpansion!";
    static final String NOT_EQUAL_PASSWORD = "Your password must be the same as verification password!";
    static final String SPECIAL_CHARACTER_PATTERN_FOR_PASSWORD =
            "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,100}";
    static final String NOT_STRONG_PASSWORD =
            "Your password is not strong! Your password have to min. 8 symbols and max. 100 symbols, " +
                    "min. one digit between 0 and 9, min. one lowercase letter, min. one uppercase letter, " +
                    "and one special character between [@#$%^&+=]";
    static final String INVALID_USER_NAME =
            "Your user name is invalid! User name have to min 8 symbols and max 20 symbols!";
    static final int MIN_NUMBER_OF_SYMBOLS_FOR_USER_NAME = 8;
    static final int MAX_NUMBER_OF_SYMBOLS_FOR_USER_NAME = 20;
    static final String FAILED_CREDENTIALS = "Validate your data is failed!";
    static final String LOGIN_MESSAGES = "You must to log in!";
    static final String WRONG_CREDENTIALS = "Your username or password is wrong!";
    static final String WRONG_INFORMATION = "Wrong information about the user or empty fields!";
    static final int MAX_NUMBER_FOR_TITLE_SIZE = 100;
    static final int MIN_NUMBER_FOR_TITLE_SIZE = 5;
    static final String SOME_OF_THE_PICTURES_DO_NOT_EXIST = "Some of the pictures do not exist or do not free!";
    public static final String THE_PICTURES_DO_NOT_EXIST = "The pictures do not exist!";
    static final String SOME_OF_THE_CATEGORIES_DO_NOT_EXIST = "Some of the categories do not exist!";
    static final String ALREADY_COMBINATION = "Exists this combination!";
    public static final String THIS_ARTICLE_IS_NOT_EXISTS = "This article is not exists!";
    static final String YOU_ARE_NOT_AUTHOR = "You are not author of this article!";
    public static final String NOT_ALLOWED_OPERATION = "The operation you want to perform is not allowed for you!";
    public static final String WRONG_REQUEST = "Invalid request!";
    public static final String NOT_EXISTS_OBJECT = "Object not found!";
}
