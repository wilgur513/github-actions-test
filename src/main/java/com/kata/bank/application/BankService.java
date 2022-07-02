package com.kata.bank.application;

import com.kata.bank.application.exception.NotEnoughBalanceException;
import com.kata.bank.application.request.DepositRequest;
import com.kata.bank.application.request.WithdrawRequest;
import com.kata.bank.domain.Transaction;
import com.kata.bank.domain.TransactionType;
import com.kata.bank.domain.Transactions;
import com.kata.bank.domain.repository.TransactionRepository;
import com.kata.bank.application.response.TransactionsResponse;
import com.kata.bank.application.response.TransactionResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BankService {

    private final TransactionRepository transactionRepository;

    public BankService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionsResponse getTransactions() {
        Transactions transactions = createTransactions();
        return new TransactionsResponse(responseListOf(transactions), transactions.getBalance());
    }

    private Transactions createTransactions() {
        List<Transaction> transactions = transactionRepository.findAllByOrderByDateAsc();
        return new Transactions(transactions);
    }

    private List<TransactionResponse> responseListOf(Transactions transactions) {
        return transactions.getValues().stream()
            .map(TransactionResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Transaction transaction = transactionRepository.save(
            new Transaction(LocalDateTime.now(), request.getAmount(), TransactionType.DEPOSIT));
        return new TransactionResponse(transaction);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        checkHasEnoughBalance(request);

        Transaction transaction = transactionRepository.save(
            new Transaction(LocalDateTime.now(), request.getAmount(), TransactionType.WITHDRAW));
        return new TransactionResponse(transaction);
    }

    private void checkHasEnoughBalance(WithdrawRequest request) {
        Transactions transactions = createTransactions();

        if (!transactions.hasEnoughBalance(request.getAmount())) {
            throw new NotEnoughBalanceException();
        }
    }
}