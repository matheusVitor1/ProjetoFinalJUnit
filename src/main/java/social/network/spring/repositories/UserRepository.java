package social.network.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import social.network.spring.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
