package social.network.spring.infra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.Transaction.TransactionRequestPostDto;
import social.network.spring.domain.dtos.Transaction.TransferRequestPostDto;
import social.network.spring.domain.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("Transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/withdraw")
    public ResponseEntity<String> executeWithdraw (@RequestBody TransactionRequestPostDto transactionRequestPostDto){
        try{
            transactionService.executeWithdraw(transactionRequestPostDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> executeDeposit (@RequestBody TransactionRequestPostDto transactionRequestPostDto){
        try{
            transactionService.executeDeposit(transactionRequestPostDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> executeTransfer (@RequestBody TransferRequestPostDto transferRequestPostDto){
        try{
            transactionService.executeTransfer(transferRequestPostDto.getSenderId(), transferRequestPostDto.getReceiverId(), transferRequestPostDto.getTransactionValue());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResponseStatusException e){
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }


}
