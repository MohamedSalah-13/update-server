package com.hamza.updateserver.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActivationRequest {
    private String activationKey;
    private String machineId;
    private String macAddress;
    private String customerEmail;
    private boolean isTrial;
}
