package com.acme.recon.extension.validation;

import com.acme.recon.model.ExecutionEvent;
import com.acme.recon.model.OrderEvent;
import org.springframework.stereotype.Component;

@Component
public class ValidatorChain {

    public ValidationResult validateOrder(OrderEvent orderEvent) {
        return ValidationResult.ok();
    }

    public ValidationResult validateExecution(ExecutionEvent executionEvent) {
        return ValidationResult.ok();
    }
}
