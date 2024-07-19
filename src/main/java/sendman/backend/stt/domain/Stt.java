package sendman.backend.stt.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sendman.backend.account.domain.Account;
import sendman.backend.common.domain.BaseTime;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stt extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String url;
    @Column(columnDefinition = "LONGTEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private LocalDate exp;
}
