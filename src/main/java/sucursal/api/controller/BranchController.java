package sucursal.api.controller;

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

    @GetMapping
    public ResponseEntity<PageDTO<BranchResponseDTO>> getAllBranches(Pageable pageable) {
        return ResponseEntity.ok(branchService.getAllBranches(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> getBranchById(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }

    @PostMapping
    public ResponseEntity<BranchResponseDTO> createBranch(@RequestBody BranchRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> updateBranch(@PathVariable Long id, @RequestBody BranchRequestDTO dto) {
        return ResponseEntity.ok(branchService.updateBranch(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}