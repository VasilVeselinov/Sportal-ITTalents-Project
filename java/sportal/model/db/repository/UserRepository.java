package sportal.model.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.db.pojo.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByUserNameOrUserEmail(String userName, String userEmail);
    User findByUserName(String userName);
}