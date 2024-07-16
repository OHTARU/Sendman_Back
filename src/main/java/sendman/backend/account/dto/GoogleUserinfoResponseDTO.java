package sendman.backend.dto.user;


public record GoogleUserinfoResponseDTO(
        String id,
        String email,
        String name
) {}