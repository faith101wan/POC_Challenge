package com.acme.recon.extension.rule;

import org.springframework.stereotype.Component;

@Component
public class MockRuleEngineAdapter implements RuleEngineAdapter {

    @Override
    public RuleDecision evaluate(Object context) {
        return RuleDecision.skipped();
    }
}
