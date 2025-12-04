package com.hamza.updateserver.repository;

import com.hamza.updateserver.model.Activation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationRepository extends JpaRepository<Activation, Integer> {

    Optional<Activation> findByActivationKey(String activationKey);

    boolean existsByActivationKeyAndIsUsedFalse(String activationKey);

    Optional<Activation> findByMachineId(String machineId);

    Optional<Activation> findByMacAddress(String macAddress);
}