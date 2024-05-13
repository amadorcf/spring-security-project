package amadorcf.es.springsecurityproject.service;

import amadorcf.es.springsecurityproject.dto.ShowPermission;
import amadorcf.es.springsecurityproject.dto.SavePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PermissionService {

    Page<ShowPermission> findAll(Pageable pageable);

    Optional<ShowPermission> findOneById(Long permissionId);

    ShowPermission createOne(SavePermission savePermission);

    ShowPermission deleteOneById(Long permissionId);
}
