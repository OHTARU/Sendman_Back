package sendman.backend.stt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.account.domain.Account;
import sendman.backend.stt.domain.Stt;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SttRepository extends JpaRepository<Stt, Long> {
    List<Stt> findByExp (LocalDate exp);
    Page<Stt> findByAccount(Account account,Pageable pageable);
}
