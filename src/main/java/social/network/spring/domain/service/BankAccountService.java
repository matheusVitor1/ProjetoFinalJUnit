package social.network.spring.domain.service;
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
import java.util.Random;


@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserRepository userRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public BankAccountDto crateBankAccountDto(BankAccount bankAccount){
        return  BankAccountDto.builder()
                .agencia(bankAccount.getAgencia())
                .digito(bankAccount.getDigito())
                .saldo(bankAccount.getSaldo())
                .userIdentity(bankAccount.getUser().getIdentity())
                .build();
    }

    public BankAccountDto findByUserId(Long userId){
        return crateBankAccountDto(bankAccountRepository.findByUser(userRepository.findById(userId)));
    }

    public boolean saveNewBankAccount(BankAccountDto bankAccountDto){
        BankAccountDto haveAccount = findByUserId(bankAccountDto.getUserID());
        if(haveAccount == null){
            BankAccount newAccount = new BankAccount(
                    bankAccountDto.getAgencia(),
                    bankAccountDto.getDigito(),
                    BigDecimal.ZERO,
                    userService.findById(bankAccountDto.getUserID())
            );

            bankAccountRepository.save(newAccount);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já cadastrado");
        }
    }

    private String generateAccountNumber(int type) {
        Random random = new Random();
        String prefixo = (type == 1) ? "CC" : "CP";
        int numeroAleatorio = 100000 + random.nextInt(900000);

        String accountGenerated = prefixo + numeroAleatorio;

        return validateAccount(accountGenerated, prefixo);
    }

    private String validateAccount(String accountGenerated, String prefixo){
        List<BankAccount> accounts = bankAccountRepository.findAll();
        boolean matchedBankAccount = accounts.stream().anyMatch(account -> account.getDigito().equals(accountGenerated));

        int type = (prefixo.equals("CC")) ? 1 : 2;

        if(matchedBankAccount){
            return generateAccountNumber(type);
        }

        return accountGenerated;
    }

    private String generateAccountAndValidateIfDuplicate( int accounType) {
        List<BankAccount> accounts = bankAccountRepository.findAll();
        Random random = new Random();

        String prefix = (accounType == 1) ? "CC" : "CP";

        int randomAccountNumber = 100000 + random.nextInt(900000);

        String accountGenerated = prefix + randomAccountNumber;

        boolean matchedBankAccount = accounts.stream().anyMatch(account -> account.getDigito().equals(accountGenerated));

        if (matchedBankAccount) {

            return generateAccountAndValidateIfDuplicate( (prefix.equals("CC")) ? 1 : 2);

        }

        return accountGenerated;
    }




}
