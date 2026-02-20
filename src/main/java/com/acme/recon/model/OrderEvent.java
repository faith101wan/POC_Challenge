package com.acme.recon.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderEvent(
        @NotBlank String orderId,
        @NotBlank String accountId,
        @Min(1) long quantity
) {
}
