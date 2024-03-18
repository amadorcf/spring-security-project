package amadorcf.es.springsecurityproject.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        SecurityFilterChain filterChain = http
                // Deshabilitamos csrf ya que se en nuestra apps utilizamos JWTs
                .csrf(csrfConfig -> csrfConfig.disable() )

                // Configuracion de sesion sin estado
                .sessionManagement( sessionManConfig ->
                        sessionManConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuracion de las estrategias con las que se hara LOGIN
                .authenticationProvider(daoAuthProvider)

                // Configuracion de las rutas
                .authorizeHttpRequests( authReqConfig ->{
                    authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
/*                    authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.POST, "/auth/validate").permitAll();*/
                    authReqConfig.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

                    // Se pueden crear mas rutas publicas...

                    // Aseguramos que el usuario tenga que estar autenticado
                    authReqConfig.anyRequest().authenticated();
                })
                .build();

        return filterChain;
    }

}
