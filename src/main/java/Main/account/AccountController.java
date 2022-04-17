package Main.account;

import Main.businesslayer.TransferMoneyService;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import Main.user.User;
import Main.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    @Qualifier("external")
    private TransferMoneyService transferMoneyServiceExternal;

    @Autowired
    @Qualifier("internal")
    private TransferMoneyService transferMoneyServiceInternal;

    @GetMapping("/account/get")
    List<Account> getAccountById(@RequestParam int id) {
        return accountRepository.findByUserId(id);
    }

    @PostMapping("/account/post")
    public void addAccount(@RequestParam String currency, @RequestParam int id, @RequestParam int money) {
        Optional<User> user = userRepository.findById(id);
        Account account = new Account(currency, user.get(), money);
        accountRepository.save(account);
    }

    /**
     * Logica de aplicare a tipului de transfer este urmatoarea: Am folosit un generator de tip UUID pentru iban, asa
     * ca m-am gandit ca daca primul caracter din iban este o cifra vom aplica un tranfer intern, iar daca acesta este
     * o litera vom aplica un tranfer extern.(Primul caracter din iban-ul sender-ului)
     */

    @PutMapping("/account/payment")
    public ResponseEntity<?> getAccountByIban(@RequestParam String ibanSender,
                                              @RequestParam String ibanReceiver,
                                              @RequestParam int money) throws NotEnoughMoneyException, AccountNotFoundException {
        Optional<Account> accountOpS = accountRepository.findByIban(ibanSender);
        Optional<Account> accountOpR = accountRepository.findByIban(ibanReceiver);

        if(Character.isDigit(ibanSender.charAt(0)) && Character.isDigit(ibanReceiver.charAt(0))) {
            transferMoneyServiceInternal.executeTransfer(accountOpS, accountOpR, money);
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (Character.isLetter(ibanReceiver.charAt(0)) || Character.isLetter(ibanSender.charAt(0))){
            transferMoneyServiceExternal.executeTransfer(accountOpS, accountOpR, money);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}