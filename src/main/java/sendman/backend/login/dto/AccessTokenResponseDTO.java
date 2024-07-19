package sendman.backend.login.dto;

import lombok.Builder;

@Builder
public record AccessTokenResponseDTO(
        String accesstoken
) {}