package social.network.spring.infra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.BankAccount.BankAccountDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.service.BankAccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("BankAccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    @PostMapping("/new")
    public ResponseEntity<String> createNewBankAccount(@RequestBody BankAccountDto bankAccountDto){
        try{
            bankAccountService.createAndSaveNewBankAccount(bankAccountDto);
            return new ResponseEntity<>("Conta criada com sucesso", HttpStatus.CREATED);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountDto> getUserBankAccount(@PathVariable("id") Long userId){
            try{
               BankAccount bankAccountFound= bankAccountService.findBankAccountByUser(userId);
               BankAccountDto dto = bankAccountService.createBankAccountDto(bankAccountFound);
               return new ResponseEntity<>(dto, HttpStatus.OK);
            } catch (ResponseStatusException e){
                return new ResponseEntity<>(null, e.getStatusCode());
            }
    }


}
