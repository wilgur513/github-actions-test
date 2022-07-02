package com.kata.bank.application.request;

import javax.validation.constraints.Min;

public class WithdrawRequest {

    @Min(1)
    private int amount;

    public WithdrawRequest() {
    }

    public WithdrawRequest(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
