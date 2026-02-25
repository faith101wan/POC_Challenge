package com.acme.recon.service;

import com.acme.recon.extension.dispatch.DispatchResult;
import com.acme.recon.extension.dispatch.DispatchStrategyFactory;
import com.acme.recon.extension.retry.RetryAdapter;
import com.acme.recon.extension.retry.RetryResult;
import com.acme.recon.extension.rule.RuleDecision;
import com.acme.recon.extension.rule.RuleEngineAdapter;
import com.acme.recon.extension.validation.ValidationResult;
import com.acme.recon.extension.validation.ValidatorChain;
import com.acme.recon.model.ExecutionEvent;
import com.acme.recon.model.OrderEvent;
import com.acme.recon.model.ReconciliationResult;
import com.acme.recon.model.ReconciliationStatus;
import com.acme.recon.repository.InMemoryReconciliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;

@Service
public class ReconciliationService {

    @Autowired
    private InMemoryReconciliationRepository repository;

    @Autowired
    private DispatchStrategyFactory dispatchStrategyFactory;

    @Autowired
    private ValidatorChain validatorChain;

    @Autowired
    private RuleEngineAdapter ruleEngineAdapter;

    @Autowired
    private RetryAdapter retryAdapter;

    public ReconciliationResult submitOrder(OrderEvent event) {
        validateOrderEvent(event);
        dispatchOrderEvent(event);

        String bizKey = bizKey(event.getAccountId(), event.getOrderId());
        repository.saveOrderQty(bizKey, event.getQuantity());

        ReconciliationResult result = new ReconciliationResult(
                event.getAccountId(),
                event.getOrderId(),
                ReconciliationStatus.MISSING_EXECUTION,
                "Order received, waiting for execution",
                Instant.now()
        );
        repository.saveResult(bizKey, result);
        return result;
    }

    public ReconciliationResult submitExecution(ExecutionEvent event) {
        validateExecutionEvent(event);
        dispatchExecutionEvent(event);

        String bizKey = bizKey(event.getAccountId(), event.getOrderId());
        Long orderQty = repository.findOrderQty(bizKey);

        if (orderQty == null) {
            ReconciliationResult result = new ReconciliationResult(
                    event.getAccountId(),
                    event.getOrderId(),
                    ReconciliationStatus.MISSING_EXECUTION,
                    "Execution arrived before order",
                    Instant.now()
            );
            repository.saveResult(bizKey, result);
            return result;
        }

        boolean firstSeenExecId = repository.addExecId(bizKey, event.getExecId());
        if (!firstSeenExecId) {
            ReconciliationResult result = new ReconciliationResult(
                    event.getAccountId(),
                    event.getOrderId(),
                    ReconciliationStatus.DUPLICATE_EXECUTION,
                    "Duplicate execution id: " + event.getExecId(),
                    Instant.now()
            );
            repository.saveResult(bizKey, result);
            return result;
        }

        long executedQty = repository.addExecutedQty(bizKey, event.getQuantity());
        ReconciliationStatus status = executedQty == orderQty
                ? ReconciliationStatus.MATCHED
                : ReconciliationStatus.MISSING_EXECUTION;

        String message = status == ReconciliationStatus.MATCHED
                ? "Order and execution quantities matched"
                : "Executed quantity " + executedQty + " / ordered quantity " + orderQty;

        ReconciliationResult result = new ReconciliationResult(
                event.getAccountId(),
                event.getOrderId(),
                status,
                message,
                Instant.now()
        );
        repository.saveResult(bizKey, result);
        return result;
    }

    public Collection<ReconciliationResult> listResults() {
        return repository.listResults();
    }

    // ===== Extension entry methods (placeholder implementation) =====

    public ValidationResult validateOrderEvent(OrderEvent event) {
        return validatorChain.validateOrder(event);
    }

    public ValidationResult validateExecutionEvent(ExecutionEvent event) {
        return validatorChain.validateExecution(event);
    }

    public DispatchResult dispatchOrderEvent(OrderEvent event) {
        return dispatchStrategyFactory.current().dispatchOrder(event);
    }

    public DispatchResult dispatchExecutionEvent(ExecutionEvent event) {
        return dispatchStrategyFactory.current().dispatchExecution(event);
    }

    public RuleDecision evaluateRuleEngine(Object context) {
        return ruleEngineAdapter.evaluate(context);
    }

    public RetryResult submitToRetry(String bizKey, Object payload) {
        return retryAdapter.submit(bizKey, payload);
    }

    private String bizKey(String accountId, String orderId) {
        return accountId + "::" + orderId;
    }
}
