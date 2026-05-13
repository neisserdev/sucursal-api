package sucursal.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sucursal.api.dto.SaleRequestDTO;
import sucursal.api.dto.SaleResponseDTO;
import sucursal.api.dto.PageDTO;
import sucursal.api.service.SaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SaleController {

    private final SaleService saleService;

    @PostMapping("/sales")
    public ResponseEntity<SaleResponseDTO> createSale(@RequestBody SaleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createSale(dto));
    }

    @GetMapping("/branches/{id}/sales")
    public ResponseEntity<PageDTO<SaleResponseDTO>> getSalesByBranch(@PathVariable("id") Long branchId, Pageable pageable) {
        return ResponseEntity.ok(saleService.getAllSalesForBranch(branchId, pageable));
    }
}