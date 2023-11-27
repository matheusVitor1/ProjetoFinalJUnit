package social.network.spring.domain.dtos.Transaction;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class TransferRequestPostDto {
    private Long senderId;
    private Long receiverId;
    private BigDecimal transactionValue;
}
