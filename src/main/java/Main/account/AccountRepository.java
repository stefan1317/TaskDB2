package Main.account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
    @Query(value = "SELECT * FROM account WHERE user_id = ?", nativeQuery = true)
    List<Account> findByUserId(int id);
}
