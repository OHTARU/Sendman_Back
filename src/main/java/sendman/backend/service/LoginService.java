package sendman.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sendman.backend.config.TokenProvider;
import sendman.backend.domain.User;
import sendman.backend.domain.UserRepository;
import sendman.backend.dto.*;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final Environment env;
    private final WebClient webClient = WebClient.builder().build();
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity<AccessTokenResponseDTO> socialLogin(String code, String registrationId) throws RuntimeException{
        //google api AT 가져오기
        String accessToken = getAccessToken(code, registrationId);
        //AT를 통해 userinfo 조회
        GoogleUserinfoResponseDTO userinfo = getUserResource(accessToken, registrationId);

        //유저 검색
        if(userRepository.findById(userinfo.id()).isPresent()){
            User user = userRepository.findById(userinfo.id()).get();
            //유저가 있을 경우 jwt 제공
            return ResponseEntity.ok().body(
                    AccessTokenResponseDTO.builder()
                            .accesstoken(tokenProvider.createAccessToken(String.format("%s:%s",user.getEmail(),"ROLE_USER")))
                            .message("구글 로그인 성공").build()
            );
        }else {
            //유저가 없을 경우 회원 저장 후 jwt 제공
            userRepository.save(User.from(userinfo));
            return ResponseEntity.created(URI.create("/login/code?"+code+"/"+registrationId)).body(
                    AccessTokenResponseDTO.builder()
                            .accesstoken(tokenProvider.createAccessToken(String.format("%s:%s",userinfo.email(),"ROLE_USER")))
                            .message("회원가입 성공").build()
            );
        }
    }
    //google의 AT 가져오는 로직
    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        //Google API - body 설정
        GoogleAccessTokenRequestDTO body = GoogleAccessTokenRequestDTO.builder()
                .code(authorizationCode)
                .client_id(clientId)
                .client_secret(clientSecret)
                .redirect_uri(redirectUri)
                .grant_type("authorization_code").build();
        //Gooogle API - Token 요청
        GoogleAccessTokenResponseDTO response = webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                    throw new RuntimeException(String.valueOf(clientResponse.bodyToMono(String.class)));
                        })
                .bodyToMono(GoogleAccessTokenResponseDTO.class)
                .block();
        return response.access_token();
    }

    private GoogleUserinfoResponseDTO getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        //Google API - 유저 정보 요청
        return webClient.get()
                .uri(resourceUri)
                .header(HttpHeaders.AUTHORIZATION,"Bearer "+accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> {
                    throw new RuntimeException(String.valueOf(clientResponse.bodyToMono(String.class)));
                        })
                .bodyToMono(GoogleUserinfoResponseDTO.class)
                .block();
    }
}
