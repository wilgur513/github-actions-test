package com.kata.bank.ui;

import static com.kata.bank.domain.TransactionType.DEPOSIT;
import static com.kata.bank.domain.TransactionType.WITHDRAW;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.kata.bank.application.BankService;
import com.kata.bank.application.exception.NotEnoughBalanceException;
import com.kata.bank.application.request.DepositRequest;
import com.kata.bank.application.request.WithdrawRequest;
import com.kata.bank.application.response.TransactionResponse;
import com.kata.bank.application.response.TransactionsResponse;
import com.kata.bank.domain.repository.TransactionRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DataJpaTest
class BankControllerTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private BankController bankController;

    @BeforeEach
    void setUp() {
        bankController = new BankController(new BankService(transactionRepository));
    }

    @Test
    void getEmptyTransactions() {
        ResponseEntity<TransactionsResponse> response = bankController.getTransactions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTransactions()).isEmpty();
        assertThat(response.getBody().getBalance()).isEqualTo(0);
    }

    @Test
    void getTransactions() {
        LocalDateTime dateOfDeposit1 = bankController.deposit(new DepositRequest(1000))
            .getBody().getDate();
        LocalDateTime dateOfDeposit2 = bankController.deposit(new DepositRequest(400))
            .getBody().getDate();
        LocalDateTime dateOfWithdraw1 = bankController.withdraw(new WithdrawRequest(500))
            .getBody().getDate();

        ResponseEntity<TransactionsResponse> response = bankController.getTransactions();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTransactions())
            .extracting(TransactionResponse::getDate, TransactionResponse::getAmount, TransactionResponse::getType)
            .containsExactly(
                tuple(dateOfDeposit1, 1000, DEPOSIT),
                tuple(dateOfDeposit2, 400, DEPOSIT),
                tuple(dateOfWithdraw1, 500, WITHDRAW)
            );

        assertThat(response.getBody().getBalance()).isEqualTo(900);
    }

    @Test
    void deposit() {
        ResponseEntity<TransactionResponse> response = bankController.deposit(new DepositRequest(1000));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDate()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(1000);
        assertThat(response.getBody().getType()).isEqualTo(DEPOSIT);
    }

    @Test
    void withdraw() {
        bankController.deposit(new DepositRequest(1000));

        ResponseEntity<TransactionResponse> response = bankController.withdraw(new WithdrawRequest(1000));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDate()).isNotNull();
        assertThat(response.getBody().getAmount()).isEqualTo(1000);
        assertThat(response.getBody().getType()).isEqualTo(WITHDRAW);
    }

    @Test
    void withdrawOverBalanceAmount() {
        assertThatThrownBy(() -> bankController.withdraw(new WithdrawRequest(1)))
            .isInstanceOf(NotEnoughBalanceException.class);
    }

    @Test
    void successTest() {
        assertThat(true).isTrue();
    }

    @Test
    void failTest() {

        assertThat(true).isFalse();
    }
}