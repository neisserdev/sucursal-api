package sucursal.api.dto;

public record BranchResponseDTO(
    Long id,
    String name,
    String address
) {}