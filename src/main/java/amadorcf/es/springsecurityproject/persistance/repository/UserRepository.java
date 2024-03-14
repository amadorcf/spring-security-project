package amadorcf.es.springsecurityproject.persistance.repository;

import amadorcf.es.springsecurityproject.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
