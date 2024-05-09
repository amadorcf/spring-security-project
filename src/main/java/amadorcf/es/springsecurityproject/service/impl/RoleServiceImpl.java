package amadorcf.es.springsecurityproject.service.impl;

import amadorcf.es.springsecurityproject.persistance.entity.security.Role;
import amadorcf.es.springsecurityproject.persistance.repository.security.RoleRepository;
import amadorcf.es.springsecurityproject.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Value("$(security.default.role)")
    private String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }

}
