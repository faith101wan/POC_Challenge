package com.acme.recon.util.service;

import com.acme.recon.extension.config.ReconciliationExtensionProperties;
import com.acme.recon.extension.dispatch.DispatchStrategyFactory;
import com.acme.recon.extension.dispatch.KafkaDispatchStrategy;
import com.acme.recon.extension.dispatch.SyncDispatchStrategy;
import com.acme.recon.extension.retry.MockRetryAdapter;
import com.acme.recon.extension.rule.MockRuleEngineAdapter;
import com.acme.recon.extension.validation.ValidatorChain;
import com.acme.recon.model.ExecutionEvent;
import com.acme.recon.model.OrderEvent;
import com.acme.recon.model.ReconciliationStatus;
import com.acme.recon.repository.InMemoryReconciliationRepository;
import com.acme.recon.service.ReconciliationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class ReconciliationServiceTest {

    private ReconciliationService service;

    @BeforeEach
    void setUp() {
        service = new ReconciliationService();

        ReflectionTestUtils.setField(service, "repository", new InMemoryReconciliationRepository());
        ReflectionTestUtils.setField(service, "validatorChain", new ValidatorChain());
        ReflectionTestUtils.setField(service, "ruleEngineAdapter", new MockRuleEngineAdapter());
        ReflectionTestUtils.setField(service, "retryAdapter", new MockRetryAdapter());
        ReflectionTestUtils.setField(
                service,
                "dispatchStrategyFactory",
                new DispatchStrategyFactory(
                        new SyncDispatchStrategy(),
                        new KafkaDispatchStrategy(),
                        new ReconciliationExtensionProperties()
                )
        );
    }

    @Test
    void shouldMarkMatchedWhenOrderAndExecutionQuantitiesAreEqual() {
        service.submitOrder(new OrderEvent("O-1", "A-1", 100));

        var result = service.submitExecution(new ExecutionEvent("E-1", "O-1", "A-1", 100));

        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.MATCHED);
    }

    @Test
    void shouldMarkDuplicateExecutionWhenExecIdAlreadyExists() {
        service.submitOrder(new OrderEvent("O-1", "A-1", 100));
        service.submitExecution(new ExecutionEvent("E-1", "O-1", "A-1", 50));

        var result = service.submitExecution(new ExecutionEvent("E-1", "O-1", "A-1", 50));

        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.DUPLICATE_EXECUTION);
    }

    @Test
    void shouldMarkMissingExecutionWhenExecutionArrivesBeforeOrder() {
        var result = service.submitExecution(new ExecutionEvent("E-9", "O-9", "A-9", 20));

        assertThat(result.getStatus()).isEqualTo(ReconciliationStatus.MISSING_EXECUTION);
    }
}
