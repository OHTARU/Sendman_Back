package sendman.backend.account.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sendman.backend.common.domain.BaseTime;
import sendman.backend.account.dto.GoogleUserinfoResponseDTO;

@Entity
@NoArgsConstructor
@Getter
public class Account extends BaseTime {
    @Id
    private String id;
    private String email;
    private String name;

    public static Account from(GoogleUserinfoResponseDTO dto){
        return Account.builder()
                .id(dto.id())
                .email(dto.email())
                .name(dto.name())
                .build();
    }
    @Builder
    public Account(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
