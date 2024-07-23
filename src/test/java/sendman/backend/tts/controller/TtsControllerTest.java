package sendman.backend.tts.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.mock.WithCustomMockUser;
import sendman.backend.tts.service.TtsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
class TtsControllerTest {

    @MockBean
    TtsService ttsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("POST `/tts/save` 테스트 (성공)")
    @WithCustomMockUser
    void ttsSave() throws Exception {
        //given
        ResponseDTO fakeDTO = new ResponseDTO("저장되었습니다",null);
        BDDMockito.given(ttsService.saveText(any(),anyString())).willReturn(fakeDTO);
        //when
        ResultActions result = mockMvc.perform(post("/tts/save?text=안녕하세요")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("저장되었습니다"));
        verify(ttsService).saveText(any(),anyString());
    }
}