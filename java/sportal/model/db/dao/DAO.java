package sportal.model.db.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public abstract class DAO {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected static final String UNSUCCESSFUL_CONNECTION_ROLLBACK = "Unsuccessful connection rollback!";
}
