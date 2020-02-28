package sportal.model.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class DAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static final String UNSUCCESSFUL_CONNECTION_ROLLBACK = "Unsuccessful connection rollback!";
}
