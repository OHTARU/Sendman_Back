package sendman.backend.dto.login;

import lombok.Builder;

@Builder
public record GoogleAccessTokenResponseDTO(
        String access_token,
        String expires_in,
        String token_type,
        String scope,
        String refresh_token
) {}