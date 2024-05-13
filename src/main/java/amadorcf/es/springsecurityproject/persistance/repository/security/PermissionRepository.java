package amadorcf.es.springsecurityproject.persistance.repository.security;

import amadorcf.es.springsecurityproject.persistance.entity.security.GrantedPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<GrantedPermission, Long> {


}
