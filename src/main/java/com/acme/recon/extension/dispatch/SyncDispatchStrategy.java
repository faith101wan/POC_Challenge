package com.acme.recon.extension.dispatch;

import org.springframework.stereotype.Component;

@Component
public class SyncDispatchStrategy implements DispatchStrategy {

    @Override
    public DispatchResult dispatchOrder(Object payload) {
        return new DispatchResult(true, "Mock sync order dispatch accepted");
    }

    @Override
    public DispatchResult dispatchExecution(Object payload) {
        return new DispatchResult(true, "Mock sync execution dispatch accepted");
    }

    @Override
    public String type() {
        return "sync";
    }
}
