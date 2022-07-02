package com.kata.bank.domain;

import java.util.function.IntUnaryOperator;

public enum TransactionType {
    WITHDRAW(amount -> -amount),
    DEPOSIT(amount -> amount);

    private final IntUnaryOperator signApplier;

    TransactionType(IntUnaryOperator signApplier) {
        this.signApplier = signApplier;
    }

    public int applySignTo(int amount) {
        return signApplier.applyAsInt(amount);
    }
}
