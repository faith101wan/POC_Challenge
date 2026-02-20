package com.acme.recon.controller;

import com.acme.recon.model.ExecutionEvent;
import com.acme.recon.model.ReconciliationResult;
import com.acme.recon.service.ReconciliationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ems/events")
public class EmsController {

    @Autowired
    private  ReconciliationService reconciliationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReconciliationResult submit(@Valid @RequestBody ExecutionEvent event) {
        return reconciliationService.submitExecution(event);
    }
}
