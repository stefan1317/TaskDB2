package Main.account;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    public boolean validateAccount(Account account, int id){
        return account.getUser().getId() == id;
    }
}
