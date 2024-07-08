package sendman.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sendman.backend.service.LoginService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/login",produces = "application/json")
public class LoginController {
    private final LoginService loginService;
    @GetMapping("/{registrationId}")
    ResponseEntity<?> OAuthLogin(@RequestParam String code, @PathVariable(value = "registrationId") String registrationId){
        return ResponseEntity.created(URI.create("/login/code?"+code+"/"+registrationId)).body(loginService.socialLogin(code, registrationId));
    }
}
