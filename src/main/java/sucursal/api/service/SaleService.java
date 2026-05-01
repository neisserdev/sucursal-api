package sucursal.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sucursal.api.dto.SaleRequestDTO;
import sucursal.api.dto.SaleResponseDTO;
import sucursal.api.dto.PageDTO;
import sucursal.api.exceptions.ResourceNotFoundException;
import sucursal.api.mapper.SaleMapper;
import sucursal.api.model.Branch;
import sucursal.api.model.Product;
import sucursal.api.model.Sale;
import sucursal.api.model.SaleItem;
import sucursal.api.repository.BranchRepository;
import sucursal.api.repository.ProductRepository;
import sucursal.api.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

        private final BranchRepository branchRepository;
        private final SaleRepository saleRepository;
        private final ProductRepository productRepository;
        private final SaleMapper saleMapper;

        @Transactional
        public SaleResponseDTO createSale(SaleRequestDTO dto) {
                Branch branch = branchRepository.findById(dto.branchId())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Branch not found with id: " + dto.branchId()));

                Sale sale = new Sale();
                sale.setBranch(branch); 

                List<SaleItem> items = dto.products().stream()
        .map(d -> {
            Product product = productRepository.findById(d.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + d.productId()));

            SaleItem item = new SaleItem(); 
            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(d.quantity());
            item.setUnitPrice(d.unitPrice());
            return item;
        })
        .toList();

                sale.setItems(new ArrayList<>(items));
                return saleMapper.toResponse(saleRepository.save(sale));
        }

        @Transactional(readOnly = true)
        public PageDTO<SaleResponseDTO> getAllSalesForBranch(Long branchID, Pageable pageable) {
                branchRepository.findById(branchID)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Branch not found with id: " + branchID));

                return PageDTO.from(saleRepository.findByBranchId(branchID, pageable)
                                .map(saleMapper::toResponse));
        }
}