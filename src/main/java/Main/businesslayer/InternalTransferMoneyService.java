package Main.businesslayer;

import Main.account.Account;
import Main.account.AccountRepository;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.beans.Transient;
import java.util.Optional;

@Service("internal")
public class InternalTransferMoneyService implements TransferMoneyService{

    @Autowired
    AccountRepository accountRepository;

    @Transient
    @Override
    public void executeTransfer(Optional<Account> sender, Optional<Account> receiver, int money) throws AccountNotFoundException, NotEnoughMoneyException {
        if(receiver.isEmpty()) {
            throw new AccountNotFoundException("The receiver account is not found.");
        }
        if(sender.isPresent()) {
            if(sender.get().getMoney() < money) {
                throw new NotEnoughMoneyException("Not enough money to send.");
            } else {
                accountRepository.updateMoney(sender.get().getMoney() - money, sender.get().getIban());
                accountRepository.updateMoney(receiver.get().getMoney() + money,receiver.get().getIban());
            }
        } else {
            throw new AccountNotFoundException("The sender account is not found.");
        }
    }
}
