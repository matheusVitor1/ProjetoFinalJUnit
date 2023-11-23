package social.network.spring.infra.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social.network.spring.domain.dtos.UserAuthenticatedDto;
import social.network.spring.domain.entities.User;
import social.network.spring.domain.service.LoginService;

@RestController
@RequestMapping("login")
public class LoginController {
    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/auth")
    public ResponseEntity<UserAuthenticatedDto> login(@RequestParam("email") String email, @RequestParam("password") String password) {

        User matchedUser = loginService.authenticateUser(email, password);
                UserAuthenticatedDto userDto = UserAuthenticatedDto.builder()
                .id(matchedUser.getId())
                .build();

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
