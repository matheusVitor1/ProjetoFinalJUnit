package social.network.spring.domain.dtos;

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
    private Long digito;
    private BigDecimal saldo;
    private String userIdentity;
    private Long userID;

}
