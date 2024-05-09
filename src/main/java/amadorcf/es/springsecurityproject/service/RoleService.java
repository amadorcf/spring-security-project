package amadorcf.es.springsecurityproject.service;

import amadorcf.es.springsecurityproject.persistance.entity.security.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findDefaultRole();

}
