package sendman.backend.voice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sendman.backend.account.domain.Account;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Voice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    private LocalDate exp;


}
