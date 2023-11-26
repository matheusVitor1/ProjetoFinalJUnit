package social.network.spring.domain.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private String name;

    @Getter
    @Column(nullable = false)
    private String birthday;

    @Getter
    @Column(unique = true, nullable = false)
    private String identity;

    @Getter
    @Column(nullable = false)
    private boolean active;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
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

    public User(Long id, String name, String birthday, String identity, boolean active, String email, String password) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.identity = identity;
        this.active = active;
        this.email = email;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

}
