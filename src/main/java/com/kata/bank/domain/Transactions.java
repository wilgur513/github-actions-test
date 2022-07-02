package com.kata.bank.domain;

import java.util.List;

public class Transactions {

    private final List<Transaction> transactions;

    public Transactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getBalance() {
        return transactions.stream()
            .mapToInt(Transaction::getSignAppliedAmount)
            .sum();
    }

    public List<Transaction> getValues() {
        return transactions;
    }

    public boolean hasEnoughBalance(int amount) {
        return getBalance() >= amount;
    }
}