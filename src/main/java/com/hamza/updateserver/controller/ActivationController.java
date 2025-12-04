package com.hamza.updateserver.controller;

import com.hamza.updateserver.model.Activation;
import com.hamza.updateserver.model.ActivationRequest;
import com.hamza.updateserver.model.ActivationResponse;
import com.hamza.updateserver.service.ActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ActivationController {

    @Autowired
    private ActivationService activationService;

    @PostMapping("/activate")
    public ResponseEntity<ActivationResponse> activate(@RequestBody ActivationRequest request) {

        // Check if key exists and is not used
        var byMachineId = activationService.findByMachineId(request.getMachineId());
        if (byMachineId.isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ActivationResponse(false, "Activation key is already used"));
        }

        Activation activation = new Activation();
        activation.setActivationKey(request.getActivationKey());
        activation.setExpirationDate(LocalDateTime.now().plusMonths(1));
        activation.setIsUsed(true);

        activation.setActivationDate(LocalDateTime.now());
        activation.setMachineId(request.getMachineId());
        activation.setMacAddress(request.getMacAddress());
        activation.setCustomerEmail(request.getCustomerEmail());
        activationService.save(activation);
        return ResponseEntity.ok(new ActivationResponse(true, "Activation successful"));

    }

    @PutMapping(value = "/update")
    public ResponseEntity<ActivationResponse> update(@RequestBody Activation request) {
        // Validate activation key
        if (request.getActivationKey() == null || request.getActivationKey().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ActivationResponse(false, "Activation key is required"));
        }

        Activation activation = new Activation();
        activation.setActivationKey(request.getActivationKey());
//        activation.setExpirationDate(LocalDateTime.now().plusMonths(1));
//        activation.setIsUsed(true);
        activation.setIsTrial(request.getIsTrial());
//        activation.setActivationDate(LocalDateTime.now());
//        activation.setMachineId(request.getMachineId());
//        activation.setMacAddress(request.getMacAddress());
//        activation.setCustomerEmail(request.getCustomerEmail());
        activationService.update(activation);
        return ResponseEntity.ok(new ActivationResponse(true, "Activation successful"));
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/activations")
    public ResponseEntity<Iterable<Activation>> allActivations() {
        return ResponseEntity.ok(activationService.allActivations());
    }

    @GetMapping("/activations/{machine_id}")
    public ResponseEntity<Activation> findByMachineId(@PathVariable String machine_id) {
        return ResponseEntity.ok(activationService.findByMachineId(machine_id).orElse(null));
    }

}

