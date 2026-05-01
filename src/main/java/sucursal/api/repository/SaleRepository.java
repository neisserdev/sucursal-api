package sucursal.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sucursal.api.model.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{
    List<Sale> findByBranchId(Long id);
    
    Page<Sale> findByBranchId(Long id, Pageable pageable);
}
