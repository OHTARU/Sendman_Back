package sendman.backend.dto;

import lombok.Builder;

public record UserinfoResponseDTO(
        String email,
        String name
) {
    @Builder
    public UserinfoResponseDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
