package com.product.start.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginPasswordDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
