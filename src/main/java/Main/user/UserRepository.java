package Main.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findById(int id);

    List<User> findByFirstName(String name);

    @Query(value = "SELECT first_name FROM user", nativeQuery = true)
    List<String> allByFirstName();

    @Query(value = "SELECT last_name FROM user", nativeQuery = true)
    List<String> allByLastName();

    @Query(value = "SELECT email FROM user", nativeQuery = true)
    List<String> allByEmail();

    @Query(value = "SELECT * FROM user" , nativeQuery = true)
    List<User> allUsers();

    Optional<User> findByEmailAndPassword(String email, String password);
}