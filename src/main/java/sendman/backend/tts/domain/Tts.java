package sendman.backend.tts.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sendman.backend.account.domain.Account;
import sendman.backend.common.domain.BaseTime;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Tts extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private LocalDate exp;
}
