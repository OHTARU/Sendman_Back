package sendman.backend.dto;

import lombok.Builder;

public record GoogleAccessTokenRequestDTO(
        String code,
        String client_id,
        String client_secret,
        String redirect_uri,
        String grant_type
) {
    @Builder
    public GoogleAccessTokenRequestDTO(String code, String client_id, String client_secret, String redirect_uri, String grant_type) {
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.grant_type = grant_type;
    }
}
