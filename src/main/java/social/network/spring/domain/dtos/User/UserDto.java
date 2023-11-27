package social.network.spring.domain.dtos.User;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class UserDto {
    public long id;
    public String name;
    public String birthday;
    public String identity;
    public boolean active;
    public String email;
    public String password;
}
