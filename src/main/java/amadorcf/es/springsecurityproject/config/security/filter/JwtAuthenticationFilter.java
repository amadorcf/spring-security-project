package amadorcf.es.springsecurityproject.config.security.filter;


import amadorcf.es.springsecurityproject.exception.ObjectNotFoundException;
import amadorcf.es.springsecurityproject.persistance.entity.security.JwtToken;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;
import amadorcf.es.springsecurityproject.persistance.repository.security.JwtTokenRepository;
import amadorcf.es.springsecurityproject.service.UserService;
import amadorcf.es.springsecurityproject.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenRepository jwtRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Lanzando JWT AUTHENTICATION FILTER");
/*
        //1. Obtener encabezado http, llamado Authorization
        String authorizationHeader = request.getHeader("Authorization");//Bearer jwt
        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        //2. Obtener token JWT desde el encabezado
        String jwt = authorizationHeader.split(" ")[1];
*/
        // Para no repetir los pasos 1 y 2 en el metodo LOGOUT creamos el siguiente metodo
        String jwt = jwtService.extractJwtFromRequest(request);
        if(jwt == null || !StringUtils.hasText(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        // 2.1 Obtener token no-expirado y valido desde BBDD
        Optional<JwtToken> token = jwtRepository.findByToken(jwt);

        // 2.2 Validar expiracion del token
        boolean isValid = validateToken(token);
        if(!isValid){
            filterChain.doFilter(request, response);
            return;
        }

        //3. Obtener el subject/username desde el token
        // esta accion a su vez valida el formato del token, firma y fecha de expiración
        String username = jwtService.extractUsername(jwt);

        //4. Setear objeto authentication dentro de security context holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );

        // Obtenemos datos como la IP o el sesionID con WebAuthenticationDetails
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        //5. Ejecutar el registro de filtros
        filterChain.doFilter(request, response);

    }

    private boolean validateToken(Optional<JwtToken> optionalToken) {

        if(!optionalToken.isPresent()){
            System.out.println("Token does not exist or invalid generation");
            return false;
        }

        JwtToken token = optionalToken.get();
        Date now = new Date(System.currentTimeMillis());

        boolean isValid = token.isValid() && token.getExpiration().after(now);

        if(!isValid){
            System.out.println("Invalid token.");
            updateTokenStatus(token);
        }
        return isValid;

    }

    private void updateTokenStatus(JwtToken token) {

        token.setValid(false);
        jwtRepository.save(token);

    }


}
