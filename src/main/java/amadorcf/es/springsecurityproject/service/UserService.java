package amadorcf.es.springsecurityproject.service;

import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.persistance.entity.User;

public interface UserService {

    User registerOneCustomer(SaveUser newUser);

}
