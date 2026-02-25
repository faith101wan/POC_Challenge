package com.acme.recon.extension.rule;

public record RuleDecision(boolean triggered, String decision, String detail) {

    public static RuleDecision skipped() {
        return new RuleDecision(false, "SKIPPED", "Mock rule engine is disabled or not implemented");
    }
}
