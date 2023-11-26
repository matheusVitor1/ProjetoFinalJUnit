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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void testFindById() {
        //Given
        User user = new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password");

        //When
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //Then
        User result = userService.findById(1L);
        assertEquals(user, result);
    }
    @Test
    void testSaveUser() {
        //When
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.save(any(User.class))).thenReturn(Mockito.mock(User.class));
        when(passwordEncoder.encode(any(String.class))).thenReturn("hashedPassword");

        //Then
        UserDto userDto = new UserDto(1L,"John Doe", "1990-01-01", "123456789",true,"john@example.com", "password" );
        boolean result = userService.saveUser(userDto);
        assertEquals(true, result);
        verify(userRepository, times(1)).save(any());
    }


    @Test
    void testSaveUser_IdentityConflict() {
        //When
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password")));

        //Then
        UserDto userDto = new UserDto(2L, "Jane Doe", "1992-03-15", "123456789", true, "jane@example.com", "password");
        assertThrows(ResponseStatusException.class, () -> userService.saveUser(userDto));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testSaveUser_EmailConflict() {
        //When
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User(1L, "John Doe", "1990-01-01", "123456789", true, "john@example.com", "password")));

        //Then
        UserDto userDto = new UserDto(2L, "Jane Doe", "1992-03-15", "987654321", true, "john@example.com", "password");
        assertThrows(ResponseStatusException.class, () -> userService.saveUser(userDto));
        verify(userRepository, never()).save(any());
    }

}
