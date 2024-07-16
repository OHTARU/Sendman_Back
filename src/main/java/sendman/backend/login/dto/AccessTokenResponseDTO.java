package sendman.backend.dto.login;

import lombok.Builder;

@Builder
public record AccessTokenResponseDTO(
        String accesstoken,
        String message
) {}