package social.network.spring.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import social.network.spring.dtos.UserDto;
import social.network.spring.entities.User;
import social.network.spring.repositories.UserRepository;
import social.network.spring.service.LoginService;
import social.network.spring.service.UserService;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext
class LoginServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Test
    void authenticateUserValidCredentialsShouldReturnUser() {
        // Given
        String email = "user@example.com";
        String password = "password";

        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("123.456.789-52")
                .active(true)
                .birthday("24/08/1999")
                .email(email)
                .password(password)
                .build();

        userService.saveUser(userDto);

        // When
        User authenticatedUser = loginService.authenticateUser(email, password);

        // Then
        assertNotNull(authenticatedUser);
        assertEquals(email, authenticatedUser.getEmail());
    }

    @Test
    @Transactional
    void authenticateUserInvalidEmailShouldThrowException() {
        // Given
        String invalidEmail = "nonexistent@example.com";

        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("987.654.321-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password(passwordEncoder.encode( "Senha123"))
                .build();
        userService.saveUser(userDto);

        // Then
        assertThrows(RuntimeException.class,
                () -> loginService.authenticateUser(invalidEmail, passwordEncoder.encode( "Senha123")));
    }

    @Test
    @Transactional
    void authenticateUserInvalidPasswordShouldThrowException() {
        // Given
        String invalidPassword = "invalid_password";

        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("987.654.321-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password(passwordEncoder.encode( "Senha123"))
                .build();
        userService.saveUser(userDto);

        // Then
        assertThrows(RuntimeException.class,
                () -> loginService.authenticateUser("mvitor5567@gmail.com", invalidPassword));
    }
}

