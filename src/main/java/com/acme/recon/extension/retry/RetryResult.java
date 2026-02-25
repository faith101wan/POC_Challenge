package com.acme.recon.extension.retry;

public record RetryResult(boolean accepted, String ticketId, String message) {

    public static RetryResult mocked() {
        return new RetryResult(true, "MOCK-TICKET-001", "Mock retry accepted");
    }
}
