package sucursal.api.controller;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sucursal.api.dto.BranchRequestDTO;
import sucursal.api.dto.BranchResponseDTO;
import sucursal.api.dto.PageDTO;
import sucursal.api.service.BranchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @Cacheable(value = "branch_list", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    @GetMapping
    public ResponseEntity<PageDTO<BranchResponseDTO>> getAllBranches(Pageable pageable) {
        return ResponseEntity.ok(branchService.getAllBranches(pageable));
    }

    @Cacheable(value = "branch_id", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @CacheEvict(value = "branch_list", allEntries = true)
    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody BranchRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(dto));
    }

    @CacheEvict(value = { "branch_list", "branch_id" }, allEntries = true)
    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Long id, @RequestBody BranchRequestDTO dto) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto));
    }

    @CacheEvict(value = { "branch_list", "branch_id" }, allEntries = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}