package social.network.spring.domain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.Transaction.TransactionRequestPostDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.Transaction;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.BankAccountService;
import social.network.spring.domain.service.TransactionService;
import social.network.spring.infra.gateway.bd.TransactionRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGetALlTransactionsByBankAccountId(){
        //When
        when(transactionRepository.findByBankAccountId(any())).thenReturn(Arrays.asList(new Transaction()));

        //Then
        List<Transaction> transactions = transactionService.getALlTransactionsByBankAccountId(1L);
        Assertions.assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    void testExecuteWithdraw_Success() {
        // Given
        BankAccount sender = new BankAccount(1234L, "CC123456", BigDecimal.valueOf(500), new User());
        when(bankAccountService.findBankAccountByUser(any())).thenReturn(sender);

        TransactionRequestPostDto transactionRequestPostDto = new TransactionRequestPostDto(BigDecimal.valueOf(50), 1L, null, null);

        // When
        boolean result = transactionService.executeWithdraw(transactionRequestPostDto);

        // Then
        assertTrue(result);
        assertEquals(BigDecimal.valueOf(450), sender.getSaldo());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testExecuteWithdraw_BankAccountNotFound() {
        // Given
        when(bankAccountService.findBankAccountByUser(1L)).thenReturn(null);

        TransactionRequestPostDto transactionRequestPostDto = new TransactionRequestPostDto(BigDecimal.valueOf(50), 1L, null, null);

        // When and Then
        assertThrows(ResponseStatusException.class, () -> transactionService.executeWithdraw(transactionRequestPostDto));
        verify(bankAccountService, times(1)).findBankAccountByUser(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testExecuteWithdraw_InsufficientBalance() {
        // Given
        when(bankAccountService.findBankAccountByUser(1L)).thenReturn(new BankAccount(1234L, "CC123456", BigDecimal.valueOf(30), new User()));

        TransactionRequestPostDto transactionRequestPostDto = new TransactionRequestPostDto(BigDecimal.valueOf(50), 1L, null, null);

        // When and Then
        assertThrows(ResponseStatusException.class, () -> transactionService.executeWithdraw(transactionRequestPostDto));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testExecuteDeposit_success(){
        // Given
        TransactionRequestPostDto transactionRequestPostDto = new TransactionRequestPostDto(BigDecimal.valueOf(50), 1L, null, null);
        BankAccount sender = new BankAccount(1234L, "CC123456", BigDecimal.valueOf(500), new User());

        //When
        when(bankAccountService.findBankAccountByUser(any())).thenReturn(sender);
        boolean result = transactionService.executeDeposit(transactionRequestPostDto);

        //Then
        assertEquals(BigDecimal.valueOf(550), sender.getSaldo());
        assertTrue(result);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

    }

    @Test
    void testExecuteDeposit_BankAccountNotFound(){
        // Given
        when(bankAccountService.findBankAccountByUser(1L)).thenReturn(null);

        TransactionRequestPostDto transactionRequestPostDto = new TransactionRequestPostDto(BigDecimal.valueOf(50), 1L, null, null);

        // When and Then
        assertThrows(ResponseStatusException.class, () -> transactionService.executeDeposit(transactionRequestPostDto));
        verify(bankAccountService, times(1)).findBankAccountByUser(1L);
        verify(transactionRepository, never()).save(any(Transaction.class));

    }

    @Test
    void testExecuteTransfer_success() {
        // Given
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transactionValue = BigDecimal.valueOf(50);

        BankAccount sender = new BankAccount(1234L, "CC123456", BigDecimal.valueOf(500), new User());
        BankAccount receiver = new BankAccount(5678L, "CC789012", BigDecimal.valueOf(200), new User());

        when(bankAccountService.findBankAccountByUser(senderId)).thenReturn(sender);
        when(bankAccountService.findBankAccountByUser(receiverId)).thenReturn(receiver);

        // When
        boolean result = transactionService.executeTransfer(senderId, receiverId, transactionValue);

        // Then
        assertTrue(result);
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        assertEquals(BigDecimal.valueOf(450), sender.getSaldo());
        assertEquals(BigDecimal.valueOf(250), receiver.getSaldo());
    }

    @Test
    void testExecuteTransfer_insufficientBalance() {
        // Given
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal transactionValue = BigDecimal.valueOf(800);

        BankAccount sender = new BankAccount(1234L, "CC123456", BigDecimal.valueOf(500), new User());
        BankAccount receiver = new BankAccount(5678L, "CC789012", BigDecimal.valueOf(200), new User());

        when(bankAccountService.findBankAccountByUser(senderId)).thenReturn(sender);
        when(bankAccountService.findBankAccountByUser(receiverId)).thenReturn(receiver);

        // When Then
        assertThrows(ResponseStatusException.class, () -> transactionService.executeTransfer(senderId, receiverId, transactionValue));
        verify(transactionRepository, never()).save(any(Transaction.class));
        assertEquals(BigDecimal.valueOf(500), sender.getSaldo());
        assertEquals(BigDecimal.valueOf(200), receiver.getSaldo());
    }


}
