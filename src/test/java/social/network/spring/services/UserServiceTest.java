package social.network.spring.services;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import social.network.spring.domain.dtos.UserDto;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.UserService;
import social.network.spring.infra.gateway.bd.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void findById() {

        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(user, result);
    }
    @Test
    void testSaveUser() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.save(any(User.class))).thenReturn(Mockito.mock(User.class));

        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");

        UserDto userDto = new UserDto(1L,"John Doe", "1990-01-01", "123456789",true,"john@example.com", "password" );

        boolean result = userService.saveUser(userDto);

        assertEquals(true, result);
    }


    @Test
    void saveUser_IdentityConflict() {

        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password")));

        UserDto userDto = new UserDto(2L, "Jane Doe", "1992-03-15", "123456789", true, "jane@example.com", "password");
        assertThrows(ResponseStatusException.class, () -> userService.saveUser(userDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void saveUser_EmailConflict() {

        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password")));

        UserDto userDto = new UserDto(2L, "Jane Doe", "1992-03-15", "987654321", true, "john@example.com", "password");
        assertThrows(ResponseStatusException.class, () -> userService.saveUser(userDto));

        verify(userRepository, never()).save(any());
    }

}
