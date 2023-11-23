package social.network.spring.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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
    private Long digito;

    @Column(nullable = false)
    private BigDecimal saldo;

    @OneToOne
    @Column(nullable = false)
    private User user;


    public BankAccount(Long agencia, Long digito, BigDecimal saldo, User user) {
        this.agencia = agencia;
        this.digito = digito;
        this.saldo = saldo;
        this.user = user;
    }
}
