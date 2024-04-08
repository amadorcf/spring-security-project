package amadorcf.es.springsecurityproject.config.security;

import amadorcf.es.springsecurityproject.config.security.filter.JwtAuthenticationFilter;
import amadorcf.es.springsecurityproject.persistance.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Necesario para autorizacion mediante anotaciones
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        SecurityFilterChain filterChain = http
                // Deshabilitamos csrf ya que en la apps se utiliza JWTs
                .csrf(csrfConfig -> csrfConfig.disable() )

                // Configuracion de sesion sin estado
                .sessionManagement( sessionManConfig ->
                        sessionManConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuracion de las estrategias con las que se hara LOGIN
                .authenticationProvider(daoAuthProvider)

                // Agregamos Filtro
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // AUTORIZACION POR PETICIONES HTTP y permisos
                // Configuracion de las rutas publicas
//                .authorizeHttpRequests( authReqConfig ->{
//
//                    buildRequestMatchersV2(authReqConfig);
//                })
                .build();

        return filterChain;
    }

    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        /*Autorizacion de endpoints de productos*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.READ_ALL_PRODUCTS.name());

        // Autorizacion mediante una expresion regular Regex
        //authReqConfig.requestMatchers(HttpMethod.GET, "/products/{productId}")
        authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/products/[0-9]*"))
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.READ_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasRole(Role.ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.CREATE_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.UPDATE_ONE_PRODUCT.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasRole(Role.ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.DISABLE_ONE_PRODUCT.name());

        /*Autorizacion de endspoints de categorias*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.READ_ALL_CATEGORIES.name());

        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.READ_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasRole(Role.ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.CREATE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.UPDATE_ONE_CATEGORY.name());

        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasRole(Role.ADMINISTRATOR.name());
                //.hasAuthority(RolePermission.DISABLE_ONE_CATEGORY.name());

        /*Autorizacion de endspoints de mi perfil*/
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(), Role.CUSTOMER.name());
                //.hasAuthority(RolePermission.READ_MY_PROFILE.name());

        /*RUTAS PUBLICAS:*/
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        //authReqConfig.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

        // Se pueden crear mas rutas publicas...

        // RUTAS PRIVADAS: Aseguramos que el usuario tenga que estar autenticado
        authReqConfig.anyRequest().authenticated();
    }

    private static void buildRequestMatchersV2(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {


        /*RUTAS PUBLICAS:*/
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
        //authReqConfig.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();

        // Se pueden crear mas rutas publicas...

        // RUTAS PRIVADAS: Aseguramos que el usuario tenga que estar autenticado
        authReqConfig.anyRequest().authenticated();
    }

}


