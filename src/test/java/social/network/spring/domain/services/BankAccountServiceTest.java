package social.network.spring.domain.services;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.BankAccount.BankAccountDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.BankAccountService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import social.network.spring.domain.service.UserService;
import social.network.spring.infra.gateway.bd.BankAccountRepository;
import social.network.spring.infra.gateway.bd.UserRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BankAccountService bankAccountService;


    @Test
    void testCreateBankAccountDto() {
        BankAccount bankAccount = new BankAccount(1L, "123", BigDecimal.TEN, new User());
        BankAccountDto bankAccountDto = bankAccountService.createBankAccountDto(bankAccount);
        assertEquals(bankAccount.getAgencia(), bankAccountDto.getAgencia());
        assertEquals(bankAccount.getDigito(), bankAccountDto.getDigito());
        assertEquals(bankAccount.getSaldo(), bankAccountDto.getSaldo());
        assertEquals(bankAccount.getUser().getIdentity(), bankAccountDto.getUserIdentity());
    }


    @Test
    void testFindBankAccountByUser() {
        BankAccount expectedBankAccount = new BankAccount();
        expectedBankAccount.setId(1L);
        when(bankAccountRepository.findByUserId(any())).thenReturn(expectedBankAccount);
        BankAccount result = bankAccountService.findBankAccountByUser(1L);
        assertEquals(expectedBankAccount, result);
    }

    @Test
    @DisplayName("Should generate unique account number")
    void testGenerateAccountNumber() {
        // Given
        BankAccount account1 = new BankAccount();
        account1.setDigito("CC123456");
        BankAccount account2 = new BankAccount();
        account2.setDigito("CP654321");

        // When
        when(bankAccountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));

        // Then
        String generatedAccountNumber = bankAccountService.generateAccountNumber(1);
        assertNotNull(generatedAccountNumber);
        assertFalse(generatedAccountNumber.equals(account1.getDigito()) || generatedAccountNumber.equals(account2.getDigito()));

        System.out.println("Generated Account Number: " + generatedAccountNumber);
    }


    @Test
    void testCreateAndSaveNewBankAccount() {
        // Given
        BankAccountDto dto = BankAccountDto.builder()
                .agencia(1234L)
                .digito("CC123456")
                .userID(1L)
                .accountType(1)
                .build();

        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");

        // When
        when(bankAccountService.findBankAccountByUser(any())).thenReturn(null);
        when(userService.findById(any())).thenReturn(user);

        // Then
        boolean result = bankAccountService.createAndSaveNewBankAccount(dto);
        assertTrue(result);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testCreateAndSaveNewBankAccount_UserNotFound() {
        // Given
        BankAccountDto dto = BankAccountDto.builder()
                .agencia(1234L)
                .digito("CC123456")
                .userID(1L)
                .accountType(1)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            bankAccountService.createAndSaveNewBankAccount(dto);
        });
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    void testCreateAndSaveNewBankAccount_AccountAlreadyExists() {
        // Given
        BankAccountDto dto = BankAccountDto.builder()
                .agencia(1234L)
                .digito("CC123456")
                .userID(1L)
                .accountType(1)
                .build();

        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");

        when(bankAccountRepository.findByUserId(any())).thenReturn(new BankAccount());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> {
            bankAccountService.createAndSaveNewBankAccount(dto);
        });
        verify(bankAccountRepository, never()).save(any());
    }

}











