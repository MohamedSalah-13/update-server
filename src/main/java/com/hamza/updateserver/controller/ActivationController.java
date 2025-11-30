package com.hamza.updateserver.controller;

import com.hamza.updateserver.model.*;
import com.hamza.updateserver.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @PostMapping("/activate")
    public ResponseEntity<ActivationResponse> activate(@RequestBody ActivationRequest request) {
        // Validate activation key against the database
//        boolean isValid = validateKey(request.getActivationKey());

        Activation activation = new Activation();
        activation.setActivationKey(request.getActivationKey());
        activation.setExpirationDate(LocalDateTime.now().plusMonths(1));
        activation.setIsUsed(true);
        activation.setActivationDate(LocalDateTime.now());
        activation.setMachineId(request.getMachineId());
        activation.setCustomerEmail(request.getCustomerEmail());
        activationService.save(activation);
        return ResponseEntity.ok(new ActivationResponse(true, "Activation successful"));
    }

    @PostMapping("/generate-key")
    public ResponseEntity<GenerateKeyResponse> generateKey(@RequestBody GenerateKeyRequest request) {
        // Generate a unique activation key
        String activationKey = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Create a new activation record
        Activation activation = new Activation();
        activation.setActivationKey(activationKey);
        activation.setIsUsed(false);
        activation.setExpirationDate(LocalDateTime.now().plusMonths(1)); // Set expiration to 1 month from now
        activation.setCustomerEmail(request.getCustomerEmail());
        // Save to databases
        activationService.save(activation);
        return ResponseEntity.ok(new GenerateKeyResponse(activationKey, "Activation key generated successfully"));
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/activations")
    public ResponseEntity<Iterable<Activation>> allActivations() {
        return ResponseEntity.ok(activationService.allActivations());
    }
}

