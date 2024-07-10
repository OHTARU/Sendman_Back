package sendman.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sendman.backend.domain.User;
import sendman.backend.domain.UserRepository;
import sendman.backend.dto.UserinfoResponseDTO;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserinfoResponseDTO getUser(org.springframework.security.core.userdetails.User userdto){
        User user = userRepository.findByEmail(userdto.getUsername()).get();
        return UserinfoResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName()).build();
    }
}
