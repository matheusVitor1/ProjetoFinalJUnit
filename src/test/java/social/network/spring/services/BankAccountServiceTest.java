package social.network.spring.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;
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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    @Autowired
    private BankAccountService bankAccountService;




    @Test
    void testFindBankAccountByIdentity() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Cria um usuário com identidade "123456789"
        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");

        // Configura o comportamento simulado do repository
        when(bankAccountRepository.findByUserId(1L)).thenReturn(Optional.of(new BankAccount()));

        // Chama o método do serviço
        Optional<BankAccount> result = bankAccountService.findByUserId(1L);

        // Verifica se o resultado não é nulo
        assertEquals(Optional.of(new BankAccount()), result);
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

