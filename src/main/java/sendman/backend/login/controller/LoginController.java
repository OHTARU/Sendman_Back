package sendman.backend.login.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sendman.backend.login.service.LoginService;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login",produces = "application/json")
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/{registrationId}")
    ResponseEntity<?> OAuthLogin(@RequestParam String code, @PathVariable(value = "registrationId") String registrationId){
        try {
            return loginService.googleLogin(code, registrationId);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e);
        }
    }


}
