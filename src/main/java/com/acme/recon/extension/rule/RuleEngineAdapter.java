package com.acme.recon.extension.rule;

public interface RuleEngineAdapter {

    RuleDecision evaluate(Object context);
}
