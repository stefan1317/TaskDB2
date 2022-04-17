package Main.account;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Query(value = "SELECT * FROM account WHERE user_id = ?", nativeQuery = true)
    List<Account> findByUserId(int id);

    Optional<Account> findByIban(String iban);

    @Transactional
    @Modifying
    @Query(value = "UPDATE account SET money = ?1 WHERE iban = ?2", nativeQuery = true)
    void updateMoney(int money, String iban);
}