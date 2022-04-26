package Main.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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

}
