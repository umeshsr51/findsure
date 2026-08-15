package com.findsure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FinderContactRequest {
    @Size(max = 120) private String name;
    @Email @Size(max = 190) private String email;
    @Size(max = 20) private String phone;
    @NotBlank @Size(max = 4000) private String message;
}
