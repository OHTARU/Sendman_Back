package sendman.backend.Image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.Image.domain.Image;
import sendman.backend.account.domain.Account;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByExp (LocalDate exp);
    List<Image> findByAccount(Account account);
}
