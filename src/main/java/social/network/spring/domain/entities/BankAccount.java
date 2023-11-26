package social.network.spring.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long agencia;

    @Column(nullable = false, unique = true)
    private String digito;

    @Column(nullable = false)
    private BigDecimal saldo;


    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    @OneToMany
    private List<Transaction> transactions;

    public BankAccount() {

    }

    public BankAccount(Long agencia, String digito, BigDecimal saldo, User user) {
        this.agencia = agencia;
        this.digito = digito;
        this.saldo = saldo;
        this.user = user;
    }
}
