package Main.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/get/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id).get();
    }

    @GetMapping("/user/get/name/{name}")
    public List<User> getUserByName(@PathVariable String name) {
        return userRepository.findByFirstName(name);
    }

    @PostMapping("/user/add")
    public void addUser(@RequestBody User user) {
        userRepository.save(user);
    }
}
