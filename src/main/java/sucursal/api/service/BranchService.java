package sucursal.api.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
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

    @Cacheable(value = "branch_list", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    @Transactional(readOnly = true)
    public PageDTO<BranchResponseDTO> getAllBranches(Pageable pageable) {
        return PageDTO.from(branchRepository.findAll(pageable).map(branchMapper::toResponse));
    }

    @Cacheable(value = "branch_id", key = "#id")
    @Transactional(readOnly = true)
    public BranchResponseDTO getBranchById(Long id) {
        return branchRepository.findById(id)
                .map(branchMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));
    }

    @CacheEvict(value = "branch_list", allEntries = true)
    @Transactional
    public BranchResponseDTO createBranch(BranchRequestDTO dto) {
        Branch saved = branchRepository.save(branchMapper.toEntity(dto));
        return branchMapper.toResponse(saved);
    }

    @CacheEvict(value = { "branch_list", "branch_id" }, allEntries = true)
    @Transactional
    public BranchResponseDTO updateBranch(Long id, BranchRequestDTO dto) {
        Branch existingBranch = branchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + id));

        existingBranch.setName(dto.name());
        existingBranch.setAddress(dto.address());

        return branchMapper.toResponse(existingBranch);
    }

    @CacheEvict(value = { "branch_list", "branch_id" }, allEntries = true)
    @Transactional
    public void deleteBranch(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new ResourceNotFoundException("Branch not found with id: " + id);
        }
        branchRepository.deleteById(id);
    }
}