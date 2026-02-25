package com.acme.recon.extension.validation;

public record ValidationResult(boolean valid, String reason) {

    public static ValidationResult ok() {
        return new ValidationResult(true, "OK");
    }
}
