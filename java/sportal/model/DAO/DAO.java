package sportal.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class DAO {

    @Autowired
    JdbcTemplate jdbcTemplate;
    static final String UNSUCCESSFUL_CONNECTION_ROLLBACK = "Unsuccessful connection rollback!";

    void setFKFalse() {
        String setFKSQL = "SET FOREIGN_KEY_CHECKS=0;";
        this.jdbcTemplate.update(setFKSQL);
    }

    void setFKTrue() {
        String setFKSQL = "SET FOREIGN_KEY_CHECKS=1;";
        this.jdbcTemplate.update(setFKSQL);
    }
}
