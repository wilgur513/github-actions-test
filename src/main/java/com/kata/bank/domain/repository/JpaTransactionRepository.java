package com.kata.bank.domain.repository;

import com.kata.bank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepository {

}
