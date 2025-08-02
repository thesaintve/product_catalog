package com.prueba.tecnica02.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CriterialWeightRequest(
        @NotBlank String criterial,
        @Min(0) double weight
) {}
