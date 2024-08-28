package sendman.backend.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sendman.backend.account.dto.AccountinfoResponseDTO;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "유저",description = "유저 정보 컨트롤러")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/info")
    @Operation(summary = "유저 정보",description = "유저 정보 조회")
    @ApiResponse(responseCode = "200", description = "유저 조회 성공")
    public ResponseEntity<AccountinfoResponseDTO> getUser(@AuthenticationPrincipal User user){
        return ResponseEntity.ok().body(accountService.getUser(user));
    }
    @DeleteMapping
    @Operation(summary = "유저 탈퇴",description = "유저 턀퇴")
    @ApiResponse(responseCode = "201", description = "유저 탈퇴 성공")
    public ResponseEntity<ResponseDTO> deleteUser(@AuthenticationPrincipal User user){
        return ResponseEntity.created(URI.create("/user")).body(accountService.deleteAccount(user));
    }
}
