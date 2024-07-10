package sendman.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sendman.backend.dto.UserinfoResponseDTO;
import sendman.backend.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<UserinfoResponseDTO> getUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(userService.getUser(user));
    }
}
