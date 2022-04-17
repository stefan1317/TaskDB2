package Main.account;

import Main.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    private String iban;
    private String currency;
    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;
    private int money;

    public Account(String currency, User user, int money) {
        this.currency = currency;
        this.user = user;
        this.money = money;
    }
}
