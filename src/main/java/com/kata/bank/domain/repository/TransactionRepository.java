package com.kata.bank.domain.repository;

import com.kata.bank.domain.Transaction;
import java.util.List;

public interface TransactionRepository {

    List<Transaction> findAllByOrderByDateAsc();

    Transaction save(Transaction transaction);
}
