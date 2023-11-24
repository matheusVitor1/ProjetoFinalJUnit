package social.network.spring.services;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import social.network.spring.domain.dtos.BankAccountDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.BankAccountService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import social.network.spring.domain.service.UserService;
import social.network.spring.infra.gateway.bd.BankAccountRepository;
import social.network.spring.infra.gateway.bd.UserRepository;
import java.math.BigDecimal;
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
    void testFindBankAccountByUser() {
        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        BankAccount expectedBankAccount = new BankAccount();
        expectedBankAccount.setId(1L);
        when(bankAccountRepository.findByUser(any())).thenReturn(expectedBankAccount);
        BankAccount result = bankAccountService.findByUserId(1L);
        assertEquals(expectedBankAccount, result);
    }



    @Test
    void testCreateBankAccountDto() {
        BankAccount bankAccount = new BankAccount(1L, "123", BigDecimal.TEN, new User());
        BankAccountDto bankAccountDto = bankAccountService.createBankAccountDto(bankAccount);
        assertEquals(bankAccount.getAgencia(), bankAccountDto.getAgencia());
        assertEquals(bankAccount.getDigito(), bankAccountDto.getDigito());
        assertEquals(bankAccount.getSaldo(), bankAccountDto.getSaldo());
        assertEquals(bankAccount.getUser().getIdentity(), bankAccountDto.getUserIdentity());
    }










}

