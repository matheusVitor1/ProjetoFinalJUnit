package social.network.spring.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String transactionType;

    @ManyToOne
    private BankAccount bankAccount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private LocalDateTime transactionDate;

    public Transaction() {
        this.transactionDate = LocalDateTime.now();
    }

    public Transaction(String transactionType, BankAccount bankAccount) {
        this.transactionType = transactionType;
        this.bankAccount = bankAccount;
        this.transactionDate =  LocalDateTime.now();
    }
}