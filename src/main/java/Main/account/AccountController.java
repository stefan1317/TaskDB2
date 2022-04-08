package Main.account;

import Main.user.User;
import Main.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/account/get/{id}")
    List<Account> getAccountById(@PathVariable int id) {
        return accountRepository.findByUserId(id);
    }

    @PostMapping("/account/post/{currency}/{id}")
    void addAccount(@PathVariable String currency, @PathVariable int id) {
        Optional<User> user = userRepository.findById(id);
        Account account = new Account(currency, user.get());
        accountRepository.save(account);
    }
}
