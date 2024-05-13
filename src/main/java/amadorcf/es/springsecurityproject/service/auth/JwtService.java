package amadorcf.es.springsecurityproject.service.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiration = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime() );

        String jwt = Jwts.builder()

                //Header
                .header()
                    .type("JWT")
                    .and()

                //Payload
                .subject(user.getUsername())

                // Signature
                .signWith(generateKey(), Jwts.SIG.HS256)

                // Extra Claims
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(extraClaims)



                .compact();

        return jwt;

    }

    // Este metodo es para decodificar el password de la base de la firma
    private SecretKey generateKey(){
        byte[] passwordDecoded = Decoders.BASE64.decode(SECRET_KEY);
        //System.out.println(new String(passwordDecoded));

        return Keys.hmacShaKeyFor(passwordDecoded);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    public Claims extractAllClaims(String jwt){
        return Jwts.parser().verifyWith( generateKey() ).build()
                .parseSignedClaims(jwt).getPayload();
    }

    public String extractJwtFromRequest(HttpServletRequest request) {

        //1. Obtener encabezado http llamado Authorization
        String authorizationHeader = request.getHeader("Authorization");//Bearer jwt
        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")){
            //filterChain.doFilter(request, response);  -->esta validacion la hacemos en el JwtAuthenticationFilter
            return null;
        }

        //2. Obtener token JWT desde el encabezado
        return authorizationHeader.split(" ")[1];

    }
}
