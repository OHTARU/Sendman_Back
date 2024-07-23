package sendman.backend.stt.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.mock.WithCustomMockUser;
import sendman.backend.stt.service.SttService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
class SttControllerTest {
    @MockBean
    SttService sttService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("POST `/stt/save` 테스트 (성공)")
    void sstSave() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file","textfile.m4a","audio/mp4","test".getBytes());
        ResponseDTO fakeDTO = new ResponseDTO("저장되었습니다",null);
        given(sttService.saveVoice(any(),any())).willReturn(fakeDTO);
        //when
        ResultActions result = mockMvc.perform(multipart("/stt/save")
                .file(file)
        );
        //then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("저장되었습니다"));
        verify(sttService).saveVoice(any(),any());

    }

    @Test
    @WithCustomMockUser
    @DisplayName("GET `/stt/list` 테스트 (성공)")
    void sttList() throws Exception {
        //given
        ResponseDTO fakeDTO = new ResponseDTO(null,any());
        given(sttService.getList(any(), 0)).willReturn(fakeDTO);
        //when
        when(sttService.getList(any(),anyInt())).thenReturn(fakeDTO);
        ResultActions result =  mockMvc.perform(get("/stt/list?page=1"));
        //then
        result.andExpect(status().isOk());
        verify(sttService).getList(any(),anyInt());
    }
}