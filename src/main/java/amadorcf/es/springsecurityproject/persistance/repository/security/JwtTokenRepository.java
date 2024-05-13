package amadorcf.es.springsecurityproject.persistance.repository.security;

import amadorcf.es.springsecurityproject.persistance.entity.security.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {



}
