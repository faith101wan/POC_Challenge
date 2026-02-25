package com.acme.recon.extension.validation;

public interface Validator<T> {

    ValidationResult validate(T payload);

    String name();
}
