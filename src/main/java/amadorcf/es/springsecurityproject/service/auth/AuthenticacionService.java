package amadorcf.es.springsecurityproject.service.auth;

import amadorcf.es.springsecurityproject.dto.RegisteredUser;
import amadorcf.es.springsecurityproject.dto.SaveUser;
import org.springframework.stereotype.Service;

// Se ha creado directamente sin Interface
@Service
public class AuthenticacionService {

    // 14-03-24: Como no se ha creado interface por defecto da una respuesta vacia
    public RegisteredUser registerOneCustomer(SaveUser newUser) {

        return null;
    }

}
