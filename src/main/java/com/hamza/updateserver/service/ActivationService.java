package com.hamza.updateserver.service;

import com.hamza.updateserver.model.Activation;
import com.hamza.updateserver.repository.ActivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActivationService {

    @Autowired
    private ActivationRepository activationRepository;

    public List<Activation> allActivations() {
        return activationRepository.findAll();
    }

    public void save(Activation activation) {
        activationRepository.save(activation);
    }

    public void update(Activation activation) {
        activationRepository.save(activation);
    }

    public boolean existsByActivationKeyAndIsUsedFalse(String key) {
        return activationRepository.existsByActivationKeyAndIsUsedFalse(key);
    }

    public Activation findByActivationKey(String key) {
        return activationRepository.findByActivationKey(key).orElse(null);
    }

    public Activation findByActivationId(Integer id) {
        return activationRepository.findById(id).orElse(null);
    }

    public Optional<Activation> findByMachineId(String machineId) {
        return activationRepository.findByMachineId(machineId);
    }

    public Optional<Activation> findByMacAddress(String macAddress) {
        return activationRepository.findByMacAddress(macAddress);
    }

    public void deleteById(Integer id) {
        activationRepository.deleteById(id);
    }
}

