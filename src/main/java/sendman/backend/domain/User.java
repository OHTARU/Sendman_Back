package sendman.backend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sendman.backend.dto.GoogleUserinfoResponseDTO;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseTimeEntity{
    @Id
    String id;
    String email;
    String name;

    public static User from(GoogleUserinfoResponseDTO dto){
        return User.builder()
                .id(dto.id())
                .email(dto.email())
                .name(dto.name())
                .build();
    }
    @Builder
    public User(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
