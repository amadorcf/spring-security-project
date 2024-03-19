package amadorcf.es.springsecurityproject.controller;

import amadorcf.es.springsecurityproject.dto.auth.AuthenticationResponse;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationRequest;
import amadorcf.es.springsecurityproject.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticationResponse res = authenticationService.login(authenticationRequest);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt){
        boolean isTokenValid = authenticationService.validateToken(jwt);

        return ResponseEntity.ok(isTokenValid);
    }

}
