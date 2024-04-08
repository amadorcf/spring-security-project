package amadorcf.es.springsecurityproject.controller;

import amadorcf.es.springsecurityproject.dto.RegisteredUser;
import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.persistance.entity.User;
import amadorcf.es.springsecurityproject.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticacionService;


    @GetMapping
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(Arrays.asList());
    }

    // DTO de respuesta (respoonse) => RegisteredUser
    // DTO de peticion (request) => SaveUser
    @PreAuthorize("permitAll")
    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser){

        // Este DTO nunca sera nulo ya que Jpa nunca devuelve una respuesta nula
        RegisteredUser registeredUser = authenticacionService.registerOneCustomer(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);

    }

}
