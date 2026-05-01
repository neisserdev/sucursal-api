package sucursal.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleResponseDTO(
    Long saleId,
    LocalDateTime time,
    BigDecimal total
) {
} 