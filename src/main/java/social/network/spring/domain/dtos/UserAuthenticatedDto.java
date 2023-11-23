package social.network.spring.domain.dtos;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserAuthenticatedDto {
    private Long id;

    private String photoUrl;

    private String name;

    private int age;

    private String birthday;

    private String identity;

    private String email;



}
