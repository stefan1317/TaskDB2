package Main.businesslayer;

import Main.account.Account;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import java.util.Optional;

public interface TransferMoneyService {
    void executeTransfer(Optional<Account> sender, Optional<Account> receiver, int money) throws AccountNotFoundException, NotEnoughMoneyException;
}
