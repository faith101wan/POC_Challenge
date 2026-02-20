package com.acme.recon.service;

import com.acme.recon.model.ExecutionEvent;
import com.acme.recon.model.OrderEvent;
import com.acme.recon.model.ReconciliationResult;
import com.acme.recon.model.ReconciliationStatus;
import com.acme.recon.repository.InMemoryReconciliationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;

@Service
public class ReconciliationService {

    private final InMemoryReconciliationRepository repository;

    public ReconciliationService(InMemoryReconciliationRepository repository) {
        this.repository = repository;
    }

    public ReconciliationResult submitOrder(OrderEvent event) {
        String bizKey = bizKey(event.accountId(), event.orderId());
        repository.saveOrderQty(bizKey, event.quantity());

        ReconciliationResult result = new ReconciliationResult(
                event.accountId(),
                event.orderId(),
                ReconciliationStatus.MISSING_EXECUTION,
                "Order received, waiting for execution",
                Instant.now()
        );
        repository.saveResult(bizKey, result);
        return result;
    }

    public ReconciliationResult submitExecution(ExecutionEvent event) {
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

    private String bizKey(String accountId, String orderId) {
        return accountId + "::" + orderId;
    }
}
