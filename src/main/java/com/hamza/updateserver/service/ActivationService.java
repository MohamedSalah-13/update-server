package com.hamza.updateserver.service;

import com.hamza.updateserver.model.Activation;
import com.hamza.updateserver.repository.ActivationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public boolean existsByActivationKeyAndIsUsedFalse(String key) {
        return activationRepository.existsByActivationKeyAndIsUsedFalse(key);
    }
}

