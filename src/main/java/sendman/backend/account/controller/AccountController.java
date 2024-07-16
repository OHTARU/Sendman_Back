package sendman.backend.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/info")
    public ResponseEntity<AccountinfoResponseDTO> getUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(accountService.getUser(user));
    }
}
