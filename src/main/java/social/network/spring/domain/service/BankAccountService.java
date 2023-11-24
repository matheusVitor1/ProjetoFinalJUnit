package social.network.spring.domain.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.BankAccountDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.User;
import social.network.spring.infra.gateway.bd.BankAccountRepository;
import social.network.spring.infra.gateway.bd.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class BankAccountService {
    @Autowired
    private final BankAccountRepository bankAccountRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public BankAccountDto createBankAccountDto(BankAccount bankAccount){

        return  BankAccountDto.builder()
                    .agencia(bankAccount.getAgencia())
                    .digito(bankAccount.getDigito())
                    .saldo(bankAccount.getSaldo())
                    .userIdentity(bankAccount.getUser().getIdentity())
                    .build();


    }

    public BankAccount findByUserId(Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        return bankAccountRepository.findByUser(optUser);
    }


    public String generateAccountNumberAndValidateIfDuplicate(int accounType)  {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        Random random = new Random();

        String prefix = (accounType == 1) ? "CC" : "CP";

        int randomAccountNumber = 100000 + random.nextInt(900000);

        String accountGenerated = prefix + randomAccountNumber;

        boolean matchedBankAccount = accounts.stream().anyMatch(account -> account.getDigito().equals(accountGenerated));

        if (matchedBankAccount) {

            return  generateAccountNumberAndValidateIfDuplicate( (prefix.equals("CC")) ? 1 : 2);
        }

        return accountGenerated;
    }

    public boolean createAndSaveNewBankAccount(BankAccountDto bankAccountDto) {
        BankAccount existingAccount = findByUserId(bankAccountDto.getUserID());
        if (existingAccount == null) {
            User user = userService.findById(bankAccountDto.getUserID());
            if (user != null) {
                String accountNumber = generateAccountNumberAndValidateIfDuplicate(bankAccountDto.getAccountType());
                BankAccount newAccount = new BankAccount(
                        bankAccountDto.getAgencia(),
                        accountNumber,
                        BigDecimal.ZERO,
                        user
                );

                bankAccountRepository.save(newAccount);
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já cadastrado");
        }
    }





}
