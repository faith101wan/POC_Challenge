package com.acme.recon.extension.retry;

public interface RetryAdapter {

    RetryResult submit(String bizKey, Object payload);
}
