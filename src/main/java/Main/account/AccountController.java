package Main.account;

import Main.businesslayer.TransferMoneyService;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import Main.config.exceptions.UnauthorizedException;
import Main.user.User;
import Main.user.UserRepository;
import Main.utils.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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

    @Autowired
    private AccountService accountService;

    @GetMapping("/account")
    public List<Account> getAccountById(@RequestParam int id) {
        return accountRepository.findByUserId(id);
    }

    @PostMapping("/account/post")
    public void addAccount(@RequestParam String currency, @RequestParam int money, @CookieValue(name = "auth") String token) {
        Jws<Claims> jwt = Utils.parseJwt(token);
        Optional<User> user = userRepository.findById(Integer.parseInt(jwt.getBody().getId()));
        Account account = new Account(currency, user.get(), money);
        accountRepository.save(account);
    }

    /**
     * Logica de aplicare a tipului de transfer este urmatoarea: Am folosit un generator de tip UUID pentru iban, asa
     * ca m-am gandit ca daca primul caracter din iban este o cifra vom aplica un tranfer intern, iar daca acesta este
     * o litera vom aplica un tranfer extern.(Primul caracter din iban-ul sender-ului)
     */

    @PutMapping("/transfer")
    public ResponseEntity<?> makeTransfer(@RequestBody AccountTransfer accountTransfer, @CookieValue(name = "auth") String token) throws NotEnoughMoneyException, AccountNotFoundException, UnauthorizedException {
        Optional<Account> accountOpS = accountRepository.findByIban(accountTransfer.getIbanSender());
        Optional<Account> accountOpR = accountRepository.findByIban(accountTransfer.getIbanReceiver());

        if (accountService.validateAccount(accountOpS.get(), Integer.parseInt(Utils.parseJwt(token).getBody().getId()))) {
            if (Character.isDigit(accountTransfer.getIbanSender().charAt(0)) && Character.isDigit(accountTransfer.getIbanReceiver().charAt(0))) {
                transferMoneyServiceInternal.executeTransfer(accountOpS, accountOpR, accountTransfer.getMoney());
                return new ResponseEntity<>(HttpStatus.OK);
            } else if (Character.isLetter(accountTransfer.getIbanReceiver().charAt(0)) || Character.isLetter(accountTransfer.getIbanSender().charAt(0))) {
                transferMoneyServiceExternal.executeTransfer(accountOpS, accountOpR, accountTransfer.getMoney());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } else {
            throw new UnauthorizedException("You are not the owner!");
        }
    }

    public void setTransferMoneyServiceExternal(TransferMoneyService transferMoneyServiceExternal) {
        this.transferMoneyServiceExternal = transferMoneyServiceExternal;
    }

    public void setTransferMoneyServiceInternal(TransferMoneyService transferMoneyServiceInternal) {
        this.transferMoneyServiceInternal = transferMoneyServiceInternal;
    }
}