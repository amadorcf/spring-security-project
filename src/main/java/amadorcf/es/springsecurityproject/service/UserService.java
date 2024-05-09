package amadorcf.es.springsecurityproject.service;

import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;

import java.util.Optional;

public interface UserService {

    User registerOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);

}
