package sucursal.api.service;

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

    @Transactional(readOnly = true)
    public PageDTO<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return PageDTO.from(productRepository.findAll(pageable).map(productMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product saved = productRepository.save(productMapper.toEntity(dto));
        return productMapper.toResponse(saved);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setName(dto.name());
        existingProduct.setDescription(dto.description());
        existingProduct.setPrice(dto.price());

        return productMapper.toResponse(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}