package sendman.backend.text.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.account.domain.Account;
import sendman.backend.text.domain.Stt;
import sendman.backend.text.domain.Text;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findByExp (LocalDate exp);
    Page<Text> findByAccount(Account account,Pageable pageable);
}
