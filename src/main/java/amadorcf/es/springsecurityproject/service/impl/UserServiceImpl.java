package amadorcf.es.springsecurityproject.service.impl;

import amadorcf.es.springsecurityproject.dto.SaveUser;
import amadorcf.es.springsecurityproject.exception.InvalidPasswordException;
import amadorcf.es.springsecurityproject.exception.ObjectNotFoundException;
import amadorcf.es.springsecurityproject.persistance.entity.security.Role;
import amadorcf.es.springsecurityproject.persistance.entity.security.User;
import amadorcf.es.springsecurityproject.persistance.repository.security.UserRepository;
import amadorcf.es.springsecurityproject.service.RoleService;
import amadorcf.es.springsecurityproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public User registerOneCustomer(SaveUser newUser) {

        // Validar contraseña: no sea nula y que coincidan
        validatePassword(newUser);

        // Validar usuario: no debe estar repetido para ello anotar en la clase User "@Column(unique = true)"
        User user = new User();

/*        String pass = passwordEncoder.encode(newUser.getPassword());
        System.out.println(pass);*/
        user.setPassword(passwordEncoder.encode(newUser.getPassword() ));
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());

        Role defaultRole = roleService.findDefaultRole()
                .orElseThrow(()-> new ObjectNotFoundException("Role not found. DeFault Role"));
        user.setRole(defaultRole);


        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(SaveUser newUser) {

        if(!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Password not found");
        }

        if(!newUser.getPassword().equals(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

    }

}
