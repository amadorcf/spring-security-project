package amadorcf.es.springsecurityproject.config.security;

import amadorcf.es.springsecurityproject.config.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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

                // Agregamos Filtro antes de que se
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Configuracion de las rutas publicas
                .authorizeHttpRequests( authReqConfig ->{
                    authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                    authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
                    //authReqConfig.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

                    // Se pueden crear mas rutas publicas...

                    // RUTAS PRIVADAS: Aseguramos que el usuario tenga que estar autenticado
                    authReqConfig.anyRequest().authenticated();
                })
                .build();

        return filterChain;
    }

}
