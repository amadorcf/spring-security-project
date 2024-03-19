package amadorcf.es.springsecurityproject.dto.auth;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String jwt;

    //Getter y Setters
    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
