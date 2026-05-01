package sucursal.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sucursal.api.dto.SaleRequestDTO;
import sucursal.api.dto.SaleResponseDTO;
import sucursal.api.model.Sale;


@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    Sale toEntity(SaleRequestDTO dto); 

    @Mapping(target = "saleId", source = "id")          
    @Mapping(target = "time", source = "createdAt")     
    @Mapping(target = "total", expression = "java(sale.getTotal())")
    SaleResponseDTO toResponse(Sale sale);
}