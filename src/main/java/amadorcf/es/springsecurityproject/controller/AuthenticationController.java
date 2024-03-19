package amadorcf.es.springsecurityproject.controller;

import amadorcf.es.springsecurityproject.dto.auth.AuthenticacionResponse;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationRequest;
import amadorcf.es.springsecurityproject.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticacionResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticacionResponse res = authenticationService.login(authenticationRequest);

        return ResponseEntity.ok(res);
    }

}
