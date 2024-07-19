package sendman.backend.tts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.account.domain.Account;
import sendman.backend.tts.domain.Tts;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TtsRepository extends JpaRepository<Tts,Long> {
    List<Tts> findByExp (LocalDate exp);
    Page<Tts> findByAccount(Account account, Pageable pageable);
}
