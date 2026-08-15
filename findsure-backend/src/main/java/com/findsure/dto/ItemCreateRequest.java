package com.findsure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCreateRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 120)
    private String name;

    @Size(max = 60)
    private String category;

    private String description;

    @Size(max = 500)
    private String photoUrl;
}
