package Main.user;

import Main.config.exceptions.UnauthorizedException;
import Main.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserController {
    UserServices userServices;
    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public User getUserById(@RequestParam int id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/user/name")
    public List<User> getUserByName(@RequestParam String name) {
        return userRepository.findByFirstName(name);
    }

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/user/initials")
    public List<String> getUserInitials() {
        List<String> firstName = userRepository.allByFirstName();
        List<String> lastName = userRepository.allByLastName();

        return userServices.getInitialsString(firstName, lastName);
    }

    @GetMapping("/user/email")
    public long getUserEmail() {
        List<String> mails = userRepository.allByEmail();

        return userServices.mailService(mails);
    }

    @GetMapping("/user/names")
    public Set<String> getUserNames() {
        List<String> names = userRepository.allByLastName();

        return userServices.lastNameService(names);
    }

    @GetMapping("/user/initialsString")
    public String getInitialsString() {
        List<String> names = userRepository.allByFirstName();

        return userServices.getStringOfInitialsService(names);
    }

    @GetMapping("/user/under20")
    public long getUserUnder20() {
        List<User> users = userRepository.allUsers();

        return userServices.getNumberOfUsersUnder20(users);
        }

    @PostMapping("/login")
    public void userLogin(@RequestBody Credentials credentials, HttpServletResponse response) throws UnauthorizedException {
        Optional<User> userOptional = userRepository.findByEmailAndPassword(credentials.getUsername(), credentials.getPassword());

        if(userOptional.isPresent()) {
            Cookie cookie = new Cookie("auth", Utils.generateToken(userOptional.get()));
            response.addCookie(cookie);
        } else {
            throw new UnauthorizedException("Credentials are wrong");
        }
    }
}
