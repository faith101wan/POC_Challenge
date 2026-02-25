package com.acme.recon.extension.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "recon")
public class ReconciliationExtensionProperties {

    private String pipelineMode = "sync";
    private boolean validationEnabled = false;
    private boolean ruleEngineEnabled = false;
    private boolean retryEnabled = false;

    public String getPipelineMode() {
        return pipelineMode;
    }

    public void setPipelineMode(String pipelineMode) {
        this.pipelineMode = pipelineMode;
    }

    public boolean isValidationEnabled() {
        return validationEnabled;
    }

    public void setValidationEnabled(boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
    }

    public boolean isRuleEngineEnabled() {
        return ruleEngineEnabled;
    }

    public void setRuleEngineEnabled(boolean ruleEngineEnabled) {
        this.ruleEngineEnabled = ruleEngineEnabled;
    }

    public boolean isRetryEnabled() {
        return retryEnabled;
    }

    public void setRetryEnabled(boolean retryEnabled) {
        this.retryEnabled = retryEnabled;
    }
}
