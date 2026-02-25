package com.acme.recon.model;

import java.time.Instant;

public class ReconciliationResult {

    private String accountId;
    private String orderId;
    private ReconciliationStatus status;
    private String message;
    private Instant updatedAt;

    public ReconciliationResult() {
    }

    public ReconciliationResult(String accountId, String orderId, ReconciliationStatus status, String message, Instant updatedAt) {
        this.accountId = accountId;
        this.orderId = orderId;
        this.status = status;
        this.message = message;
        this.updatedAt = updatedAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
