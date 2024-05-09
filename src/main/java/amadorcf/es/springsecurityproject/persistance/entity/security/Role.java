package amadorcf.es.springsecurityproject.persistance.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @OneToMany(mappedBy = "role") // por defecto feth type es LAZY
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @JsonIgnore //Necesario para evitar la serializacion infinita del Json
    private List<GrantedPermission> permissions;


    // Getter y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GrantedPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<GrantedPermission> permissions) {
        this.permissions = permissions;
    }
}
