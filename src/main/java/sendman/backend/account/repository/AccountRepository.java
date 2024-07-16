package sendman.backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.account.domain.Account;
import sendman.backend.domain.User;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
}
