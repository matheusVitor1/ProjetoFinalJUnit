package social.network.spring.infra.gateway.bd;

import org.springframework.data.jpa.repository.JpaRepository;
import social.network.spring.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
