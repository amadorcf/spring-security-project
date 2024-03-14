package amadorcf.es.springsecurityproject.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

// como va a viajar a traves del protocolo Http, implementamos con serializable
public class SaveCategory implements Serializable {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
