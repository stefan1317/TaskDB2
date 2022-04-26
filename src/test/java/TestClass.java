import Main.account.*;
import Main.businesslayer.ExternalTransferMoneyService;
import Main.businesslayer.InternalTransferMoneyService;
import Main.config.exceptions.UnauthorizedException;
import Main.user.UserServices;
import Main.config.exceptions.AccountNotFoundException;
import Main.config.exceptions.NotEnoughMoneyException;
import Main.user.User;
import Main.user.UserController;
import Main.user.UserRepository;
import Main.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AccountRepository.class, UserRepository.class})
@ExtendWith(MockitoExtension.class)
public class TestClass {
    @InjectMocks
    AccountController accountController = new AccountController();

    @InjectMocks
    UserController userController = new UserController(new UserServices());

    @Mock
    AccountService accountService;

    @Mock
    UserServices userServices;

    @Mock
    AccountRepository accountRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void testAccountGet() {
        List<Account> list = new ArrayList<>();
        list.add(new Account());
        when(accountRepository.findByUserId(1)).thenReturn(list);
        assertEquals(accountController.getAccountById(1), list);
    }

    @Test
    public void testAccountAdd() {
        Optional<User> userOP = Optional.of(new User());
        Account account = new Account("test", userOP.get(), 12);
        when(userRepository.findById(anyInt())).thenReturn(userOP);
        accountController.addAccount("test", 12, Utils.generateToken(userOP.get()));
        verify(accountRepository).save(account);
    }

    @Test
    public void testAccountTransferExternal() throws NotEnoughMoneyException, AccountNotFoundException, UnauthorizedException {
        Optional<Account> optionalAccount1 = Optional.of(new Account());
        optionalAccount1.get().setIban("2s2312");
        optionalAccount1.get().setMoney(200);
        Optional<Account> optionalAccount2 = Optional.of(new Account());
        optionalAccount2.get().setIban("1233");
        optionalAccount2.get().setMoney(20);
        when(accountRepository.findByIban(anyString())).thenReturn(optionalAccount1);
        when(accountRepository.findByIban(anyString())).thenReturn(optionalAccount2);
        AccountTransfer accountTransfer= new AccountTransfer(optionalAccount1.get().getIban(),
                optionalAccount2.get().getIban(), 1);
        accountController.setTransferMoneyServiceInternal(new InternalTransferMoneyService(accountRepository));
        assertEquals(accountController.makeTransfer(accountTransfer, Utils.generateToken(new User())).
                getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAccountTransferInternal() throws NotEnoughMoneyException, AccountNotFoundException, UnauthorizedException {
        Optional<Account> optionalAccount1 = Optional.of(new Account());
        optionalAccount1.get().setIban("as2312");
        optionalAccount1.get().setMoney(200);
        Optional<Account> optionalAccount2 = Optional.of(new Account());
        optionalAccount2.get().setIban("b233");
        optionalAccount2.get().setMoney(20);
        when(accountRepository.findByIban(anyString())).thenReturn(optionalAccount1);
        when(accountRepository.findByIban(anyString())).thenReturn(optionalAccount2);
        AccountTransfer accountTransfer= new AccountTransfer(optionalAccount1.get().getIban(),
                optionalAccount2.get().getIban(), 1);
        accountController.setTransferMoneyServiceExternal(new ExternalTransferMoneyService(accountRepository));
        assertEquals(accountController.makeTransfer(accountTransfer, Utils.generateToken(new User())).
                getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAccountTransferConflict() throws NotEnoughMoneyException, AccountNotFoundException, UnauthorizedException {
        Optional<Account> optionalAccount1 = Optional.of(new Account());
        optionalAccount1.get().setIban("#s2312");
        optionalAccount1.get().setMoney(200);
        Optional<Account> optionalAccount2 = Optional.of(new Account());
        optionalAccount2.get().setIban("@233");
        optionalAccount2.get().setMoney(20);
        AccountTransfer accountTransfer= new AccountTransfer(optionalAccount1.get().getIban(),
                optionalAccount2.get().getIban(), 1);
        assertEquals(accountController.makeTransfer(accountTransfer, Utils.generateToken(new User())).
                getStatusCode(), HttpStatus.CONFLICT);
    }

    @Test
    public void testUserGetId() {
        Optional<User> userOP = Optional.of(new User());
        when(userRepository.findById(1)).thenReturn(userOP);
        assertEquals(userController.getUserById(1), userOP.get());
    }

    @Test
    public void testUserGetName() {
        List<User> list = new ArrayList<>();
        list.add(new User());
        when(userRepository.findByFirstName(anyString())).thenReturn(list);
        assertEquals(userController.getUserByName(anyString()), list);
    }

    @Test
    public void testUserAdd() {
        User user = new User();
        userController.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    public void testUserInitials() {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> finalList = new ArrayList<>();

        when(userRepository.allByFirstName()).thenReturn(list1);
        when(userRepository.allByLastName()).thenReturn(list2);
        when(userServices.getInitialsString(list1, list2)).thenReturn(finalList);
        assertEquals(userController.getUserInitials(), finalList);
    }

    @Test
    public void testUserMail() {
        List<String> list = new ArrayList<>();
        long count = 0;

        when(userRepository.allByEmail()).thenReturn(list);
        when(userServices.mailService(list)).thenReturn(count);
        assertEquals(userController.getUserEmail(), count);
    }

    @Test
    public void testUserGetUsernames() {
        List<String> list = new ArrayList<>();
        Set<String> set = new TreeSet<>();

        when(userServices.lastNameService(list)).thenReturn(set);
        assertEquals(userController.getUserNames(), set);
    }

    @Test
    public void testUserInitialString() {
        List<String> list = new ArrayList<>();
        String s = "test";

        when(userRepository.allByFirstName()).thenReturn(list);
        when(userServices.getStringOfInitialsService(list)).thenReturn(s);
        assertEquals(userController.getInitialsString(), s);

    }

    @Test
    public void testUserUnder20() {
        List<User> list = new ArrayList<>();
        long count = 0;

        when(userRepository.allUsers()).thenReturn(list);
        when(userServices.getNumberOfUsersUnder20(list)).thenReturn(count);
        assertEquals(userController.getUserUnder20(), count);

    }
}