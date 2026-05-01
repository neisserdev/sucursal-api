package sucursal.api.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import sucursal.api.exceptions.ResourceNotFoundException;
import sucursal.api.model.Product;
import sucursal.api.model.SaleItem;
import sucursal.api.repository.BranchRepository;
import sucursal.api.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {

        private final SaleRepository saleRepository;
        private final BranchRepository branchRepository;

        public Optional<Product> getBestSellingProduct() {
                return saleRepository.findAll().stream()
                                .flatMap(sale -> sale.getItems().stream())
                                .collect(Collectors.groupingBy(
                                                SaleItem::getProduct,
                                                Collectors.summingInt(SaleItem::getSafeQuantity)))
                                .entrySet().stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey);
        }

        public BigDecimal getRevenueForBranch(Long branchId) {
                if (!branchRepository.existsById(branchId)) {
                        throw new ResourceNotFoundException("Branch not found with id: " + branchId);
                }

                return saleRepository.findByBranchId(branchId).stream()
                                .flatMap(sale -> sale.getItems().stream())
                                .map(SaleItem::getSubtotal)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
}