package sucursal.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sucursal.api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
