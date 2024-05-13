package amadorcf.es.springsecurityproject.persistance;

import amadorcf.es.springsecurityproject.dto.RegisteredUser;
import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationResponse;
import amadorcf.es.springsecurityproject.dto.auth.AuthenticationRequest;
import amadorcf.es.springsecurityproject.exception.ObjectNotFoundException;
import amadorcf.es.springsecurityproject.persistance.entity.security.JwtToken;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;
import amadorcf.es.springsecurityproject.persistance.repository.security.JwtTokenRepository;
import amadorcf.es.springsecurityproject.service.UserService;
import amadorcf.es.springsecurityproject.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// Se ha creado directamente sin Interface
@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenRepository jwtRepository;


    // 14-03-24: Como no se ha creado interface por defecto da una respuesta vacia
    // Se corrige lo anterior devolviendo un dto del usuario con el correspondiente JWT
    public RegisteredUser registerOneCustomer(SaveUser newUser) {

        // Registrar un nuevo cliente, previamente se crea un usuario
        User user = userService.registerOneCustomer(newUser);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName()); //Nombre de la enumeracion

        // Generamos el JWT
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    // Metodo para hacer en LOGIN
    public AuthenticationResponse login(AuthenticationRequest authRequest) {

        // Crear objeto por LOGIN
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        // Proceso del LOGIN
        authenticationManager.authenticate(authentication);

        // Obtener detalles del usuario
        UserDetails user = userService.findOneByUsername(authRequest.getUsername()).get();

        // Crear jwt del usuario
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));

        // Generar respuesta
        AuthenticationResponse authRes = new AuthenticationResponse();
        authRes.setJwt(jwt);

        return authRes;
    }

    public boolean validateToken(String jwt) {

        // Valida el header, la firma y el tiempo de validacion
        try{
            jwtService.extractUsername(jwt);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public User findLoggedInUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth instanceof UsernamePasswordAuthenticationToken authToken){
            String username = (String) authToken.getPrincipal();

            return userService.findOneByUsername(username)
                    .orElseThrow(() ->  new ObjectNotFoundException("User not found, ", username));
        }

        return null; // Esta instancia nunca se va a ejecutar ya que siempre se va a cumplir la condicion del if. Lo mantengo por si tuvieramos otras implementaciones de Authentication

    }

    public void logout(HttpServletRequest request) {

        String jwt = jwtService.extractJwtFromRequest(request);
        if(jwt == null || !StringUtils.hasText(jwt)) return;

        Optional<JwtToken> token = jwtRepository.findByToken(jwt);

        if(token.isPresent() && token.get().isValid()){
            token.get().setValid(false);
            jwtRepository.save(token.get());
        }
    }
}
