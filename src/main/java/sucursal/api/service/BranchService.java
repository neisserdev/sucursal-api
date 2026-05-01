package sucursal.api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sucursal.api.dto.BranchRequestDTO;
import sucursal.api.dto.BranchResponseDTO;
import sucursal.api.dto.PageDTO;
import sucursal.api.exceptions.ResourceNotFoundException;
import sucursal.api.mapper.BranchMapper;
import sucursal.api.model.Branch;
import sucursal.api.repository.BranchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;

    @Transactional(readOnly = true)
    public PageDTO<BranchResponseDTO> getAllBranches(Pageable pageable) {
        return PageDTO.from(branchRepository.findAll(pageable).map(branchMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public BranchResponseDTO getBranchById(Long id) {
        return branchRepository.findById(id)
                .map(branchMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
    }

    @Transactional
    public BranchResponseDTO createBranch(BranchRequestDTO dto) {
        Branch saved = branchRepository.save(branchMapper.toEntity(dto));
        return branchMapper.toResponse(saved);
    }

    @Transactional
    public BranchResponseDTO updateBranch(Long id, BranchRequestDTO dto) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        existingBranch.setName(dto.name());
        existingBranch.setAddress(dto.address());

        return branchMapper.toResponse(existingBranch);
    }

    @Transactional
    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }
}