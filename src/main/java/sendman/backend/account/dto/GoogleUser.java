package sendman.backend.dto.user;


public record GoogleUser(
        String id,
        String email,
        String name,
        String picture,
        String locale
) {
}
