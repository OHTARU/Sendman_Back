package sendman.backend.login.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sendman.backend.account.domain.Account;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.config.SecurityConfig;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.filter.JwtAuthenticationFilter;
import sendman.backend.common.service.TokenProvider;
import sendman.backend.login.service.LoginService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
class LoginControllerTest {
    @MockBean
    LoginService loginService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    @DisplayName("GET `/login` 테스트 (성공)")
    void OAuthLogin_success() throws Exception {
        //given
        String url = "/login/google?code=testData";
        ResponseDTO fakedto = new ResponseDTO("로그인 성공", null);
        BDDMockito.given(loginService.googleLogin(anyString(),anyString())).willReturn(fakedto);
        //when
        final ResultActions reselt =  mockMvc.perform(get(url));
        //then
        reselt.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"));
        verify(loginService).googleLogin(anyString(),anyString());
    }
}