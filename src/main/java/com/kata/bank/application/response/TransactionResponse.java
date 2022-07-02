package com.kata.bank.application.response;

import com.kata.bank.domain.Transaction;
import com.kata.bank.domain.TransactionType;
import java.time.LocalDateTime;

public class TransactionResponse {

    private LocalDateTime date;
    private int amount;
    private TransactionType type;

    public TransactionResponse(Transaction transaction) {
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }
}
