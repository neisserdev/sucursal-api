package sucursal.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sucursal.api.model.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
}