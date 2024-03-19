package amadorcf.es.springsecurityproject.dto.auth;

import java.io.Serializable;

public class AuthenticacionResponse implements Serializable {

    private String jwt;

    //Getter y Setters
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
