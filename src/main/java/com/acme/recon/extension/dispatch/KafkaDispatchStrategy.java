package com.acme.recon.extension.dispatch;

import org.springframework.stereotype.Component;

@Component
public class KafkaDispatchStrategy implements DispatchStrategy {

    @Override
    public DispatchResult dispatchOrder(Object payload) {
        return new DispatchResult(true, "Mock kafka order publish accepted");
    }

    @Override
    public DispatchResult dispatchExecution(Object payload) {
        return new DispatchResult(true, "Mock kafka execution publish accepted");
    }

    @Override
    public String type() {
        return "async";
    }
}
