package amadorcf.es.springsecurityproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class SavePermission implements Serializable {

    @NotBlank
    private String role;

    @NotBlank
    private String operation;


    //Getter y setters
    public @NotBlank String getRole() {
        return role;
    }

    public void setRole(@NotBlank String role) {
        this.role = role;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}
