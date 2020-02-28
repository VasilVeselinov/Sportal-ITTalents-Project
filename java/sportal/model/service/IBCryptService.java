package sportal.model.service;

public interface IBCryptService {

    String cryptPassword(String text);

    boolean checkPassword(String text, String dbText);
}
