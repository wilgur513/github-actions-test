package com.kata.bank.application.response;

import java.util.List;

public class TransactionsResponse {

    private List<TransactionResponse> transactions;
    private int balance;

    public TransactionsResponse(List<TransactionResponse> transactions, int balance) {
        this.transactions = transactions;
        this.balance = balance;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    public int getBalance() {
        return balance;
    }
}
