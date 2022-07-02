package com.kata.bank.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.kata.bank.domain.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    void findAllByOrderByDateAsc() {
        LocalDateTime earlyDateTime = LocalDateTime.now();
        LocalDateTime lateDateTime = LocalDateTime.now();
        Transaction lateTransaction = transactionRepository.save(new Transaction(lateDateTime, 400, TransactionType.DEPOSIT));
        Transaction earlyTransaction = transactionRepository.save(new Transaction(earlyDateTime, 1000, TransactionType.DEPOSIT));

        List<Transaction> transactions = transactionRepository.findAllByOrderByDateAsc();

        assertThat(transactions)
            .extracting(Transaction::getId, Transaction::getDate, Transaction::getAmount,
                transaction -> transaction.getType().name())
            .containsExactly(
                tuple(earlyTransaction.getId(), earlyDateTime, 1000, "DEPOSIT"),
                tuple(lateTransaction.getId(), lateDateTime, 400, "DEPOSIT")
            );
    }
    
    @Test
    void fail() {
        assertThat(true).isFalse();
    }
}
