package com.hamza.updateserver.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GenerateKeyRequest {
    private String customerEmail;
    private Boolean isTrial; // nullable -> defaults to false when not provided
}
