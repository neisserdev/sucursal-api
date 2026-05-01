package sucursal.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sucursal.api.dto.ProductRequestDTO;
import sucursal.api.dto.ProductResponseDTO;
import sucursal.api.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "saleItems", ignore = true)
    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponse(Product product);
}
