package com.acme.recon.controller;

import com.acme.recon.model.ReconciliationResult;
import com.acme.recon.service.ReconciliationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/reconciliation/results")
public record QueryController(ReconciliationService service) {

    @GetMapping
    public Collection<ReconciliationResult> list() {
        return service.listResults();
    }
}
