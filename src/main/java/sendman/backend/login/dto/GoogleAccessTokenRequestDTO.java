package sendman.backend.dto.login;

import lombok.Builder;

@Builder
public record GoogleAccessTokenRequestDTO(
        String code,
        String client_id,
        String client_secret,
        String redirect_uri,
        String grant_type
) {}