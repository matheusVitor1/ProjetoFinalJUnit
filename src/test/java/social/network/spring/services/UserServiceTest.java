package social.network.spring.services;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import social.network.spring.domain.dtos.UserDto;
import social.network.spring.infra.gateway.bd.UserRepository;
import social.network.spring.domain.service.UserService;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void testCalculateAge() {
        int age = userService.calculateAge("24/08/1999");
        assertEquals(24, age);
    }


    @Test
    @Transactional
    public void testSaveUser() {
        // Given
        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("123.456.789-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password("Senha123")
                .build();

        // When
        boolean createdUser = userService.saveUser(userDto);

        // Then (Então)
        assertTrue(createdUser);
    }

    @Test
    @Transactional
    public void testSaveWithDuplicateIdentity() throws ResponseStatusException{
        //Given
        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("123.456.789-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password("Senha123")
                .build();

        userService.saveUser(userDto);

        UserDto duplicateDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("123.456.789-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor@gmail.com")
                .password("Senha123")
                .build();

        //When
        boolean duplicatedUser = userService.saveUser(duplicateDto);

        //Then
        assertFalse(duplicatedUser);


    }
    @Test
    @Transactional
    public void testSaveWithDuplicateEmail() throws ResponseStatusException{

        //Given
        UserDto userDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("987.654.321-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password("Senha123")
                .build();
       userService.saveUser(userDto);

        UserDto duplicatedDto = UserDto.builder()
                .name("Matheus Vitor")
                .photoUrl("https//:minhafoto.jpg")
                .identity("123.456.789-52")
                .active(true)
                .birthday("24/08/1999")
                .email("mvitor5567@gmail.com")
                .password("Senha123")
                .build();

        //When
        boolean duplicatedUser = userService.saveUser(duplicatedDto);

        //Then
        assertFalse(duplicatedUser);
    }

}
