package sendman.backend.login.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.login.service.LoginService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
        ResponseDTO fakeDTO = new ResponseDTO("로그인 성공", null);
        BDDMockito.given(loginService.googleLogin(anyString(),anyString())).willReturn(fakeDTO);
        //when
        final ResultActions result =  mockMvc.perform(get(url));
        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"));
        verify(loginService).googleLogin(anyString(),anyString());
    }
}