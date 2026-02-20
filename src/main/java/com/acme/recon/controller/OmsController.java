package com.acme.recon.controller;

import com.acme.recon.model.OrderEvent;
import com.acme.recon.model.ReconciliationResult;
import com.acme.recon.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oms/events")
public record OmsController(ReconciliationService service) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReconciliationResult submit(@Valid @RequestBody OrderEvent event) {
        return service.submitOrder(event);
    }
}
