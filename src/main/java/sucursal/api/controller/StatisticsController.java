package sucursal.api.controller;

import java.math.BigDecimal;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sucursal.api.dto.ProductResponseDTO;
import sucursal.api.mapper.ProductMapper;
import sucursal.api.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ProductMapper productMapper;

    @Cacheable(value = "statistics_best_selling")
    @GetMapping("/best-selling-product")
    public ResponseEntity<ProductResponseDTO> bestSellingProduct() {

        return statisticsService.getBestSellingProduct()
                .map(productMapper::toResponse)
                .map(dto -> ResponseEntity.ok(dto))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Cacheable(value = "statistics_revenue", key = "#id")
    @GetMapping("/branches/{id}/revenue")
    public ResponseEntity<BigDecimal> revenueByBranch(@PathVariable Long id) {
        return ResponseEntity.ok(statisticsService.getRevenueForBranch(id));
    }
}