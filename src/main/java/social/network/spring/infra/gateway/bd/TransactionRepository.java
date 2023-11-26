package social.network.spring.infra.gateway.bd;

import org.springframework.data.jpa.repository.JpaRepository;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.Transaction;

import java.util.List;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBankAccountId(Long bankAccountId);

}
