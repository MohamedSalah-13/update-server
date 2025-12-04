package com.hamza.updateserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
 public class GenerateKeyResponse {
    private String activationKey;
    private Boolean isTrial;
    private String message;
}
