package Main.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountTransfer {
    private String ibanSender;
    private String ibanReceiver;
    private int money;

    public AccountTransfer(String ibanSender, String ibanReceiver, int money) {
        this.ibanSender = ibanSender;
        this.ibanReceiver = ibanReceiver;
        this.money = money;
    }
}
