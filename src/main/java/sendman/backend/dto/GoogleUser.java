package sendman.backend.dto;

import lombok.Getter;


public record GoogleUser(
        String id,
        String email,
        String name,
        String picture,
        String locale
) {
}
