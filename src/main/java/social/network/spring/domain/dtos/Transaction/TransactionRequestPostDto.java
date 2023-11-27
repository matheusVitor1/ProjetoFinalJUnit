package social.network.spring.domain.dtos.Transaction;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class TransactionRequestPostDto {
    private BigDecimal transactionValue;
    private Long userId;
    private Long senderId;
    private Long receiverId;
}
