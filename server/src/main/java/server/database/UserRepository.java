package server.database;

import commons.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByUserName(String userName);

    boolean existsByUserName(String userName);

}
