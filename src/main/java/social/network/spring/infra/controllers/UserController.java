package social.network.spring.infra.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.network.spring.domain.dtos.UserAuthenticatedDto;
import social.network.spring.domain.dtos.UserDto;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> createUser (@RequestBody UserDto userDto){
        boolean newUser = userService.saveUser(userDto);
        Map<String, String> response = new HashMap<>();
        if(newUser){
            response.put("message", "usuário criado");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "usuário já cadastrado");
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAuthenticatedDto> getUserInformation(@PathVariable("id") Long id) {
        User user = userService.findById(id);

        if (user != null) {
            UserAuthenticatedDto userDto = userService.createAuthenticatedUserDto(user);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
