package com.acme.recon.repository;

import com.acme.recon.model.ReconciliationResult;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryReconciliationRepository {

    private final Map<String, Long> orderQtyByBizKey = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> execIdsByBizKey = new ConcurrentHashMap<>();
    private final Map<String, Long> executedQtyByBizKey = new ConcurrentHashMap<>();
    private final Map<String, ReconciliationResult> latestResultByBizKey = new ConcurrentHashMap<>();

    public void saveOrderQty(String bizKey, long quantity) {
        orderQtyByBizKey.put(bizKey, quantity);
    }

    public Long findOrderQty(String bizKey) {
        return orderQtyByBizKey.get(bizKey);
    }

    public boolean addExecId(String bizKey, String execId) {
        return execIdsByBizKey
                .computeIfAbsent(bizKey, key -> ConcurrentHashMap.newKeySet())
                .add(execId);
    }

    public long addExecutedQty(String bizKey, long qtyToAdd) {
        return executedQtyByBizKey.merge(bizKey, qtyToAdd, Long::sum);
    }

    public void saveResult(String bizKey, ReconciliationResult result) {
        latestResultByBizKey.put(bizKey, result);
    }

    public Collection<ReconciliationResult> listResults() {
        return new ArrayList<>(latestResultByBizKey.values());
    }
}
