package social.network.spring.infra.gateway.bd;
import org.springframework.data.jpa.repository.JpaRepository;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.User;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByUserId(Long userId);

}
