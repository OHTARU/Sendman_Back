package sendman.backend.tts.controller;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import sendman.backend.common.mock.WithCustomMockUser;
import sendman.backend.tts.service.TtsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.yml")
class ImageControllerTest {

    @MockBean
    TtsService ttsService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithCustomMockUser
    @DisplayName("POST `/tts/save` 이미지 업로드 서비스 호출 테스트 (성공)")
    void imageSave() throws Exception {
        //Given
        final String file = "test.png";
        MockMultipartFile image = new MockMultipartFile("file",file,
                "image/png", "<<png data>>".getBytes());

        when(ttsService.saveImage(any(),any())).thenReturn(true);
        //When & Then
        mockMvc.perform(multipart("/tts/save")
                    .file(image)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .accept(MediaType.APPLICATION_JSON)
                    .characterEncoding("UTF-8"))
            .andDo(
                    MockMvcResultHandlers.log()
            )
            .andExpect(status().isCreated())
            .andExpect(content().string("true"));
        verify(ttsService).saveImage(any(),any());
    }
}