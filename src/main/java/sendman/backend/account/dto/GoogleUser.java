package sendman.backend.account.dto;


public record GoogleUser(
        String id,
        String email,
        String name,
        String picture,
        String locale
) {
}
