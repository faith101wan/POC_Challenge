package com.acme.recon.extension.retry;

import org.springframework.stereotype.Component;

@Component
public class MockRetryAdapter implements RetryAdapter {

    @Override
    public RetryResult submit(String bizKey, Object payload) {
        return RetryResult.mocked();
    }
}
