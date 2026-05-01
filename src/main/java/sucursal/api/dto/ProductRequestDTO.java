package sucursal.api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
    @NotBlank(message = "The name of product is required")
    String name,

    @Size(min = 10, max = 250, message = "The minimum description is 10 characters")
    String description,

    @NotNull(message = "The price of product is required")  
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    BigDecimal price
) {}