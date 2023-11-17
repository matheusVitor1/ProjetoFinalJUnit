package social.network.spring.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.dtos.LoginDto;
import social.network.spring.dtos.UserDto;
import social.network.spring.entities.User;
import social.network.spring.repositories.UserRepository;

import java.util.List;
@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    public LoginService(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public User authenticateUser(String email, String password) {
        List <User> users = this.userService.getALL();
        User matchedUser = users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));


        if (passwordEncoder.matches(password, matchedUser.getPassword())) {
            return matchedUser;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha incorreta");
        }
    }

}
