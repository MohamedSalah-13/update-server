package com.hamza.updateserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ActivationResponse {
    private boolean success;
    private String message;
    private String licenseKey;
}
