package sendman.backend.account.dto;

import lombok.Builder;

@Builder
public record AccountinfoResponseDTO(
        String email,
        String name
) {}