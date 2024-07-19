package sendman.backend.login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import sendman.backend.account.domain.Account;
import sendman.backend.account.repository.AccountRepository;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.service.TokenProvider;
import sendman.backend.login.dto.AccessTokenResponseDTO;
import sendman.backend.login.dto.GoogleAccessTokenRequestDTO;
import sendman.backend.login.dto.GoogleAccessTokenResponseDTO;
import sendman.backend.account.dto.GoogleUserinfoResponseDTO;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final Environment env;
    private final WebClient webClient = WebClient.builder().build();
    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;

    public ResponseDTO googleLogin(String code, String registrationId){
        //google api AT 가져오기
        String accessToken = getAccessToken(code, registrationId);
        //AT를 통해 userinfo 조회
        GoogleUserinfoResponseDTO userinfo = getUserResource(accessToken, registrationId);

        //유저 검색
        if(accountRepository.findById(userinfo.id()).isPresent()){
            Account user = accountRepository.findById(userinfo.id()).get();
            //유저가 있을 경우 jwt 제공
            return new ResponseDTO("로그인 성공",
                            new AccessTokenResponseDTO(
                                    tokenProvider.createAccessToken(String.format("%s:%s",user.getEmail(),"ROLE_USER")))
            );
        }else {
            //유저가 없을 경우 회원 저장 후 jwt 제공
            accountRepository.save(Account.from(userinfo));
            return new ResponseDTO("회원가입 성공",
                    new AccessTokenResponseDTO(
                            tokenProvider.createAccessToken(String.format("%s:%s", userinfo.email(), "ROLE_USER")))
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
