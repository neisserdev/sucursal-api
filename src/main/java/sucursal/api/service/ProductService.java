package sucursal.api.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sucursal.api.dto.ProductRequestDTO;
import sucursal.api.dto.ProductResponseDTO;
import sucursal.api.dto.PageDTO;
import sucursal.api.exceptions.ResourceNotFoundException;
import sucursal.api.mapper.ProductMapper;
import sucursal.api.model.Product;
import sucursal.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(value = "product_list", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    @Transactional(readOnly = true)
    public PageDTO<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return PageDTO.from(productRepository.findAll(pageable).map(productMapper::toResponse));
    }

    @Cacheable(value = "product_id", key = "#id")
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @CacheEvict(value = "product_list", allEntries = true)
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product saved = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toResponse(saved);
    }

    @CacheEvict(value = { "product_list", "product_id" }, allEntries = true)
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setName(dto.name());
        existingProduct.setDescription(dto.description());
        existingProduct.setPrice(dto.price());

        return productMapper.toResponse(existingProduct);
    }

    @CacheEvict(value = { "product_list", "product_id" }, allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}