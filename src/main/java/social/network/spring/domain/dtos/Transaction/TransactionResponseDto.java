package social.network.spring.domain.dtos.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDto {

    private Long transferId;
    private String transactionType;
    private LocalDateTime transactionDate;
    private BigDecimal transactionValue;



}
