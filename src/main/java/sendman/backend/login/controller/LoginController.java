package sendman.backend.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.login.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login",produces = "application/json")
@Tag(name = "로그인",description = "OAuth2 로그인 컨트롤러")
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/{registrationId}")
    @Operation(summary = "유저 로그인",description = "유저 로그인 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    ResponseEntity<ResponseDTO> OAuthLogin(@RequestParam String code, @PathVariable(value = "registrationId") String registrationId){
        try {
            return ResponseEntity.ok().body(loginService.googleLogin(code,registrationId));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(
                    new ResponseDTO(null,e.getMessage()));
        }
    }
}
