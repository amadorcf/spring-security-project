package amadorcf.es.springsecurityproject.persistance.repository;

import amadorcf.es.springsecurityproject.persistance.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {


}
