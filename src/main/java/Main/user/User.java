package Main.user;

import lombok.Getter;
import javax.persistence.*;

@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String password;
}
