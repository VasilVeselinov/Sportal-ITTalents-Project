package sportal.model.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sportal.model.db.pojo.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUsernameOrUserEmail(String userName, String userEmail);

    Optional<User> findByUsername(String userName);

    Optional<User> findByToken(String token);

    @Query(value = "SELECT id, username, password, user_email, is_enabled, token " +
            "FROM users LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<User> findAllByPages(int limit, int offset);
}
