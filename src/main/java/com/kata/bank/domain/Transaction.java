package com.kata.bank.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private int amount;

    private TransactionType type;

    protected Transaction() {
    }

    public Transaction(LocalDateTime date, int amount, TransactionType type) {
        this(null, date, amount, type);
    }

    public Transaction(Long id, LocalDateTime date, int amount, TransactionType type) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getSignAppliedAmount() {
        return type.applySignTo(amount);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }
}
