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

        var trial = request.isTrial();
        // إذا لم تكن نسخة تجريبية يتم البحث عن البيانات
        if (!trial) {
            // Check if key exists and is not used
            var byMachineId = activationService.findByMachineId(request.getMachineId());
            if (byMachineId.isPresent()) {
                var activation = byMachineId.get();
                if (activation.getActivationKey().equals(request.getActivationKey()) && activation.getMachineId().equals(request.getMachineId()))
                    return ResponseEntity.badRequest()
                            .body(new ActivationResponse(false, "Activation key is already used"));
            }
        }


        Activation activation = new Activation();
        activation.setActivationKey(request.getActivationKey());
        activation.setExpirationDate(LocalDateTime.now().plusMonths(1));
        activation.setIsUsed(true);

        activation.setIsTrial(trial);
        activation.setActivationDate(LocalDateTime.now());
        activation.setMachineId(request.getMachineId());
        activation.setMacAddress(request.getMacAddress());
        activation.setCustomerEmail(request.getCustomerEmail());
        activationService.save(activation);
        return ResponseEntity.ok(new ActivationResponse(true, "Activation successful"));

    }


    /**
     * Updates the activation information for a machine ID based on the provided request data.
     * Validates the activation key and checks for an existing activation entry before updating.
     * يستخدم فى حالة إذا كان هذا الجهاز سبق له عمل نسخة تجريببة ويتم عمل تحديث بياناته او تفعيل البرنامج
     *
     * @param request the activation details containing the activation key, machine ID, and trial status
     * @return a ResponseEntity containing an ActivationResponse indicating the success or failure of the update
     */
    @PutMapping(value = "/update")
    public ResponseEntity<ActivationResponse> update(@RequestBody Activation request) {
        // Validate activation key
        if (request.getActivationKey() == null || request.getActivationKey().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ActivationResponse(false, "Activation key cannot be empty or null"));
        }


        var byMachineId = activationService.findByMachineId(request.getMachineId());
        if (byMachineId.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ActivationResponse(false, "No activation found for this machine id"));
        }

        Activation activation = new Activation();
        activation.setActivationKey(request.getActivationKey());
        activation.setIsTrial(false);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        activationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}

