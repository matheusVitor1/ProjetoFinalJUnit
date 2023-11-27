package social.network.spring.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.User.UserAuthenticatedDto;
import social.network.spring.domain.dtos.User.UserDto;
import social.network.spring.domain.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import social.network.spring.infra.gateway.bd.UserRepository;
import java.util.List;


@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAuthenticatedDto createAuthenticatedUserDto(User user) {

        return UserAuthenticatedDto.builder()
                .id(user.getId())
                .name(user.getName())
                .birthday(user.getBirthday())
                .identity(user.getIdentity())
                .email(user.getEmail())
                .build();
    }

    public List<User> getALL(){
        return userRepository.findAll();
    }

    public User findById(Long id){
        return this.userRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Usuário não encontrado"
                        )
                );
    }
    public boolean findByIdentity(List<User> users, String identidade) {
        return users.stream().anyMatch(user -> user.getIdentity().equals(identidade));
    }

    public boolean findByEmail(List<User> users, String email) {
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public boolean saveUser(UserDto userDto) {
        List<User> users = getALL();
        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        User user = new User(
                userDto.getName(),
                userDto.getBirthday(),
                userDto.getIdentity(),
                userDto.getEmail(),
                hashedPassword,
                userDto.isActive()
        );
        if (findByIdentity(users, userDto.getIdentity())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Identidade já cadastrada");
        }
        else if (findByEmail(users, userDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        }
        else {
            userRepository.save(user);
            return true;
        }
    }



    
}
