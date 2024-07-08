package sendman.backend.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User extends BaseTimeEntity{
    @Id
    Long id;
    String email;
    String name;

    public User from(Long id,String email, String name){
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }
    @Builder
    public User(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
