package io.github.rivon0507.optimalassignment.backend.dto;

import jakarta.validation.constraints.*;

public record AssignmentRequest(
        @NotNull @Pattern(regexp = "min|max") String optimization,
        @NotNull @NotEmpty @Size(min = 1) long[][] matrix
) {
}
