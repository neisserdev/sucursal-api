package sucursal.api.dto;

import java.math.BigDecimal;

public record SaleItemDTO(
    Long productId,
    Integer quantity,      
    BigDecimal unitPrice
) {}