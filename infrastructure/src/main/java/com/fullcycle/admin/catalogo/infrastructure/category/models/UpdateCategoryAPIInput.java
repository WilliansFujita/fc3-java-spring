package com.fullcycle.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryAPIInput(
        @JsonProperty("name") String name,
        @JsonProperty("decription") String description,
        @JsonProperty("is_active") Boolean active
) {
}
