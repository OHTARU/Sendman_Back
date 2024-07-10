package sendman.backend.dto;

import lombok.Builder;

public record AccessTokenResponseDTO(
        String accesstoken,
        String message
) {
    @Builder
    public AccessTokenResponseDTO(String accesstoken, String message) {
        this.accesstoken = accesstoken;
        this.message = message;
    }
}
