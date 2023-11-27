package social.network.spring.domain.dtos.BankAccount;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class BankAccountDto {

    private Long id;
    private Long agencia;
    private String digito;
    private BigDecimal saldo;
    private int accountType;
    private String userIdentity;
    private Long userID;

}
