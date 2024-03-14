package amadorcf.es.springsecurityproject.persistance.repository;

import amadorcf.es.springsecurityproject.persistance.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
