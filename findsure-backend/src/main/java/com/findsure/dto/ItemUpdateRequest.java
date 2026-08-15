package com.findsure.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemUpdateRequest {

    @Size(max = 120)
    private String name;

    @Size(max = 60)
    private String category;

    private String description;

    @Size(max = 500)
    private String photoUrl;
}
