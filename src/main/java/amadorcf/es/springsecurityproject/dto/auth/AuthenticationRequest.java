package amadorcf.es.springsecurityproject.dto.auth;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {

    private String username;
    private String password;

    //Getter y Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

