package sendman.backend.account.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AccountinfoResponseDTO(
        String email,
        String name
) {}