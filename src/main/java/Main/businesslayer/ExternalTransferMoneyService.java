package Main.businesslayer;

import Main.account.Account;
import Main.account.AccountRepository;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("external")
public class ExternalTransferMoneyService implements TransferMoneyService{
    AccountRepository accountRepository;

    @Autowired
    public ExternalTransferMoneyService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void executeTransfer(Optional<Account> sender, Optional<Account> receiver, int money) throws AccountNotFoundException, NotEnoughMoneyException {
        if(sender.isEmpty()) {
            throw new AccountNotFoundException("The sender account is not found.");
        } else {
            if(sender.get().getMoney() < money) {
                throw new NotEnoughMoneyException("Not enough money to send.");
            } else {
                accountRepository.updateMoney(sender.get().getMoney() - money, sender.get().getIban());
            }
        }
    }
}
