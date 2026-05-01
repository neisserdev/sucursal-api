package sucursal.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequestDTO(
    @NotBlank(message = "Branch name is required")
    String name,
    @NotBlank(message = "Branch address is required")
    @Size(min = 10, message = "Branch address must be at least 10 characters long")
    String address
) {}