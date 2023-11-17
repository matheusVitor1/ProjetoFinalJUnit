package social.network.spring.dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class UserDto {
    public long id;
    public String photoUrl;
    public String name;
    public int age;
    public String birthday;
    public String identity;
    public boolean active;
    public String email;
    public String password;
}
