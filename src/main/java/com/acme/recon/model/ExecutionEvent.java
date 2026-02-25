package com.acme.recon.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ExecutionEvent {

    @NotBlank
    private String execId;

    @NotBlank
    private String orderId;

    @NotBlank
    private String accountId;

    @Min(1)
    private long quantity;

    public ExecutionEvent() {
    }

    public ExecutionEvent(String execId, String orderId, String accountId, long quantity) {
        this.execId = execId;
        this.orderId = orderId;
        this.accountId = accountId;
        this.quantity = quantity;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
