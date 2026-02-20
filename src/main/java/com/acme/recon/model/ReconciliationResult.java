package com.acme.recon.model;

import java.time.Instant;

public record ReconciliationResult(
        String accountId,
        String orderId,
        ReconciliationStatus status,
        String message,
        Instant updatedAt
) {
}
