package amadorcf.es.springsecurityproject.exception;

import amadorcf.es.springsecurityproject.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception, HttpServletRequest request){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setMessage("Internal error, try again");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    // Exccepcion 403 para cuando no se tienen los permisos de rol necesarios
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerGenericException(AccessDeniedException exception,
                                                     HttpServletRequest request){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setMessage("Access denied. You don not have the necessary permissions to access this feature. Ex" +
                "Please, contact with the admin if you think that this is an error");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    // Esta excepcion se lanza cuando no se logra hacer el binding del json al objeto Java
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception,
                                                                    HttpServletRequest request){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setMessage("Error en la peticion enviada");
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setTimestamp(LocalDateTime.now());


        System.out.println(exception.getAllErrors()
                .stream()
                .map(each -> each.getDefaultMessage())
                .collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}
