package com.acme.recon.extension.dispatch;

import com.acme.recon.extension.config.ReconciliationExtensionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DispatchStrategyFactory {

    @Autowired
    private  SyncDispatchStrategy syncDispatchStrategy;
    @Autowired
    private  KafkaDispatchStrategy kafkaDispatchStrategy;
    @Autowired
    private  ReconciliationExtensionProperties properties;



    public DispatchStrategy current() {
        if ("async".equalsIgnoreCase(properties.getPipelineMode())) {
            return kafkaDispatchStrategy;
        }
        return syncDispatchStrategy;
    }
}
