package sendman.backend.account.dto;


public record GoogleUserinfoResponseDTO(
        String id,
        String email,
        String name
) {}