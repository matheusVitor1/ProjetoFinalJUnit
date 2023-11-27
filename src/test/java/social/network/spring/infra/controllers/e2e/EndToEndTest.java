package social.network.spring.infra.controllers.e2e;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import social.network.spring.domain.dtos.BankAccount.BankAccountDto;
import social.network.spring.domain.dtos.User.UserDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.BankAccountService;
import social.network.spring.domain.service.UserService;
import social.network.spring.infra.gateway.bd.BankAccountRepository;
import social.network.spring.infra.gateway.bd.UserRepository;

import static org.mockito.Mockito.*;


@SpringBootTest()
public class EndToEndTest {

    @Autowired
    private UserService userService;
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;


    @Test
    void createUserAndBankAccount (){
        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .identity("123.456.789-10")
                .birthday("24/08/1999")
                .email("matheus_vitoralves@hotmail.com")
                .password("senha123")
                .active(true)
                .build();

       boolean userCreated = userService.saveUser(userDto);

        BankAccountDto bankAccountDto = BankAccountDto.builder()
                .accountType(1)
                .agencia(1234L)
                .userID(1L)
                .build();

        boolean bankAccountCreated = bankAccountService.createAndSaveNewBankAccount(bankAccountDto);

      Assertions.assertTrue(userCreated);
      Assertions.assertTrue(bankAccountCreated);

    }
}
