package com.kata.bank.application.request;

import javax.validation.constraints.Min;

public class DepositRequest {

    @Min(value = 1)
    private int amount;

    public DepositRequest() {
    }

    public DepositRequest(@Min(value = 1) int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
