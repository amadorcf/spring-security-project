package amadorcf.es.springsecurityproject.controller;

import amadorcf.es.springsecurityproject.dto.RegisteredUser;
import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticacionService;

    // DTO de respuesta (respoonse) => RegisteredUser
    // DTO de peticion (request) => SaveUser
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser){

        // Este DTO nunca sera nulo ya que Jpa nunca devuelve una respuesta nula
        RegisteredUser registeredUser = authenticacionService.registerOneCustomer(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    }

}
