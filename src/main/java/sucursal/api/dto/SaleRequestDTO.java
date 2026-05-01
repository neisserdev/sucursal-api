package sucursal.api.dto;

import java.util.List;

public record SaleRequestDTO(
    Long branchId,
    List<SaleItemDTO> products
) {}