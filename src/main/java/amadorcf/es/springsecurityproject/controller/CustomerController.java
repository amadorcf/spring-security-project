package amadorcf.es.springsecurityproject.controller;

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
    private AuthenticacionService authenticacionService;

    @PostMapping
    public ResponseEntity<RegisterUser> registerOne(@RequestBody @Valid SaveUser newUser){

        // Este DTO nunca sera nulo ya que Jpa nunca devuelve una respuesta nula
        RegisteredUser registeredUser = authenticacionService.registerOneCustomer(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    }

}
