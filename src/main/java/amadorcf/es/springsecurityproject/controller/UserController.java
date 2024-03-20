package amadorcf.es.springsecurityproject.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*  METODO ALTERNATIVO: Crear el endpoint /users y obtener el usuario logeado
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{username}")
    public void checkUser(){
        throw new AccessDeniedException();
        return null;
    }

}
*/
