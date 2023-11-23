package social.network.spring.domain.dtos;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class LoginDto {
    private String email;
    private String password;
}
