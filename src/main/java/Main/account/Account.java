package Main.account;

import Main.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "Account{" +
                "iban='" + iban + '\'' +
                ", currency='" + currency + '\'' +
                ", user=" + user +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return money == account.money && Objects.equals(iban, account.iban) && Objects.equals(currency, account.currency) && Objects.equals(user, account.user);
    }
}
