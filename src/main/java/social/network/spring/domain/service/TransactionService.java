package social.network.spring.domain.service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.Transaction.TransactionRequestPostDto;
import social.network.spring.domain.dtos.Transaction.TransactionResponseDto;
import social.network.spring.domain.entities.BankAccount;
import social.network.spring.domain.entities.Transaction;
import social.network.spring.infra.gateway.bd.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

     private final BankAccountService bankAccountService;
     private final TransactionRepository transactionRepository;

    public TransactionService(BankAccountService bankAccountService, TransactionRepository transactionRepository) {
        this.bankAccountService = bankAccountService;
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getALlTransactionsByBankAccountId(Long id){
        return transactionRepository.findByBankAccountId(id);
    }



    public boolean executeWithdraw(TransactionRequestPostDto transactionRequestPostDto) {
        BankAccount accountFound = bankAccountService.findBankAccountByUser(transactionRequestPostDto.getUserId());

        if (accountFound == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
        }

        if (accountFound.getSaldo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Saldo insuficiente");
        }

        if (transactionRequestPostDto.getTransactionValue().compareTo(accountFound.getSaldo()) > 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Valor é maior que o que tem em conta");
        }

        accountFound.setSaldo(accountFound.getSaldo().subtract(transactionRequestPostDto.getTransactionValue()));
        Transaction newTransaction = new Transaction("Withdraw", accountFound);
        transactionRepository.save(newTransaction);
        return true;
    }

    public boolean executeDeposit(TransactionRequestPostDto transactionRequestPostDto){
        BankAccount accountFound = bankAccountService.findBankAccountByUser(transactionRequestPostDto.getUserId());

        if (accountFound == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada");
        }

        accountFound.setSaldo(accountFound.getSaldo().add(transactionRequestPostDto.getTransactionValue()));
        Transaction newTransaction = new Transaction("Deposit", accountFound);
        transactionRepository.save(newTransaction);
        return true;
    }

    public boolean executeTransfer(Long senderId, Long receiverId, BigDecimal transactionValue){
        BankAccount sender = bankAccountService.findBankAccountByUser(senderId);
        BankAccount receiver = bankAccountService.findBankAccountByUser(receiverId);

        if(sender == null || receiver == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não foi possível realizar a Operação");
        }

        if(sender.getSaldo().compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Saldo insuficiente");
        }

        if (transactionValue.compareTo(sender.getSaldo()) > 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Valor é maior que o que tem em conta");
        }

        sender.setSaldo(sender.getSaldo().subtract(transactionValue));
        receiver.setSaldo(receiver.getSaldo().add(transactionValue));
        Transaction saveSenderTransaction = new Transaction("Transfer", sender);
        Transaction saveReceiverTransaction = new Transaction("Transfer", receiver);
        transactionRepository.save(saveSenderTransaction);
        transactionRepository.save(saveReceiverTransaction);
        return true;
    }



}
