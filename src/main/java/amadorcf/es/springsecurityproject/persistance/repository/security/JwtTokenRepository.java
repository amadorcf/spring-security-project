package amadorcf.es.springsecurityproject.persistance.repository.security;

import amadorcf.es.springsecurityproject.persistance.entity.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {


    Optional<JwtToken> findByToken(String jwt);
}
