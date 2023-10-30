package security.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.demo.Entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

   // Optional<User> findByName(String username);

    //Optional<User> findByUserName(String username);

    Optional<User> findByusername(String username);
}
