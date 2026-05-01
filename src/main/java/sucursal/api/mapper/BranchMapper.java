package sucursal.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import sucursal.api.dto.BranchRequestDTO;
import sucursal.api.dto.BranchResponseDTO;
import sucursal.api.model.Branch;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sales", ignore = true) 
    Branch toEntity(BranchRequestDTO dto);

    BranchResponseDTO toResponse(Branch entity);
}