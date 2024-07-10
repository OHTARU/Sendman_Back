package sendman.backend.dto;

public record GoogleAccessTokenResponseDTO(
        String access_token,
        String expires_in,
        String token_type,
        String scope,
        String refresh_token
) {
}
