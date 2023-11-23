package social.network.spring.domain.entities;
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

    @Column(nullable = false)
    private String name;

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

    @OneToOne(mappedBy = "user")
    private BankAccount bankAccount;


    public User() {

    }

    public User(String name, String birthday, String identity, String email, String password, boolean active) {
        this.name = name;
        this.birthday = birthday;
        this.identity = identity;
        this.active = active;
        this.email = email;
        this.password = password;
    }
}
