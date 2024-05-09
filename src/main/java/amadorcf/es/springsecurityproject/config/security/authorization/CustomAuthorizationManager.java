package amadorcf.es.springsecurityproject.config.security.authorization;

import amadorcf.es.springsecurityproject.exception.ObjectNotFoundException;
import amadorcf.es.springsecurityproject.persistance.entity.security.Operation;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;
import amadorcf.es.springsecurityproject.persistance.repository.security.OperationRepository;
import amadorcf.es.springsecurityproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestContext) {

        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);

        if(isPublic){
            return new AuthorizationDecision(true);
        }

        boolean isGranted = isGranted(url, httpMethod, authentication.get());
        System.out.println("IS GRANTED: "+ isGranted);

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {

        // Validar authenticacion
        if( authentication == null || !(authentication instanceof UsernamePasswordAuthenticationToken)){
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = getOperations(authentication);


        return operations.stream().anyMatch(getOperationPredicate(url, httpMethod));
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {
            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);

            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private List<Operation> getOperations(Authentication authentication) {

        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken) authentication;
        String username = (String) authToken.getPrincipal();

        User user = userService.findOneByUsername(username)
                .orElseThrow(()-> new ObjectNotFoundException("User not found. Username: " + username));

        return user.getRole().getPermissions().stream()
                .map(grantedPermission -> grantedPermission.getOperation())
                .collect(Collectors.toList());


    }


    private boolean isPublic(String url, String httpMethod){

        List<Operation> publicAccessEndpoints = operationRepository
                .findByPublicAccess();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("IS PUBLIC: "+ isPublic);

        return isPublic;
    }

    private String extractUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath(); //api/v1
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");

        System.out.println(url);

        return url;
    }

}
