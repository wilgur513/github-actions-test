package com.kata.bank.ui;

import com.kata.bank.application.BankService;
import com.kata.bank.application.exception.NotEnoughBalanceException;
import com.kata.bank.application.request.DepositRequest;
import com.kata.bank.application.request.WithdrawRequest;
import com.kata.bank.application.response.TransactionResponse;
import com.kata.bank.application.response.TransactionsResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<Void> handleNotEnoughBalance() {
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getTransactions() {
        TransactionsResponse response = bankService.getTransactions();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = bankService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = bankService.withdraw(request);
        return ResponseEntity.ok(response);
    }
}
