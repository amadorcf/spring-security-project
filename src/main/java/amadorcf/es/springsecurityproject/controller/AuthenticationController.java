package amadorcf.es.springsecurityproject.controller;

import amadorcf.es.springsecurityproject.dto.LogoutResponse;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationResponse;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationRequest;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;
import amadorcf.es.springsecurityproject.persistance.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PreAuthorize("permitAll")
    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt){
        boolean isTokenValid = authenticationService.validateToken(jwt);

        return ResponseEntity.ok(isTokenValid);
    }

    //@CrossOrigin // Necesario para habilitar el intercambio de recursos entre origenes diferentes
    @PreAuthorize("permitAll")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticationResponse res = authenticationService.login(authenticationRequest);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request){
        authenticationService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("Logout completed."));
    }

    @PreAuthorize("hasAuthority('READ_MY_PROFILE')")
    @GetMapping("/profile")
    public ResponseEntity<User> FindMyProfile(){
        User user = authenticationService.findLoggedInUser();

        return ResponseEntity.ok(user);
    }


}
