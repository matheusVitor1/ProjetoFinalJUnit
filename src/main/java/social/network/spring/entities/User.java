package social.network.spring.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String photoUrl;

    private String userWallpaper;

    private String userSelfDescription;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String birthday;

    @Column(unique = true, nullable = false)
    private String identity;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    public User() {

    }

    public User(String photoUrl, String name, int age, String birthday, String identity, String email, String password, boolean active) {
        this.photoUrl = photoUrl;
        this.name = name;
        this.age = age;
        this.birthday = birthday;
        this.identity = identity;
        this.active = active;
        this.email = email;
        this.password = password;
    }
}
