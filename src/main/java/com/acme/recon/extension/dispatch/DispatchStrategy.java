package com.acme.recon.extension.dispatch;

public interface DispatchStrategy {

    DispatchResult dispatchOrder(Object payload);

    DispatchResult dispatchExecution(Object payload);

    String type();
}
