package amadorcf.es.springsecurityproject.service.auth;

import amadorcf.es.springsecurityproject.dto.RegisteredUser;
import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.persistance.entity.User;
import amadorcf.es.springsecurityproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Se ha creado directamente sin Interface
@Service
public class AuthenticacionService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    // 14-03-24: Como no se ha creado interface por defecto da una respuesta vacia
    // Se corrige lo anterior devolviendo un dto del usuario con el correspondiente JWT
    public RegisteredUser registerOneCustomer(SaveUser newUser) {

        // Registrar un nuevo cliente, previamente se crea un usuario
        User user = userService.registerOneCustomer(newUser);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().name()); //Nombre de la enumeracion

        // Generamos el JWT
        String jwt = jwtService.generateToken(user);
        userDto.setJwt(jwt);

        return userDto;
    }

}
