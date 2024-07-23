package sendman.backend.tts.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.tts.service.TtsService;

import java.net.URI;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "TTS",description = "TTS API 컨트롤러")
public class TtsController {
    private final TtsService ttsService;

    @PostMapping("/save")
    @Operation(summary = "TTS 내용 저장", description = "TTS 내용 저장 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "TTS 내용 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 정보 오류"),
            @ApiResponse(responseCode = "500",description = "서버 내부 오류")
    })
    public ResponseEntity<?> ttsSave(@AuthenticationPrincipal User user,@RequestParam(value = "text")String text){
        return ResponseEntity.created(URI.create("/tts/save")).body(ttsService.saveText(user, text));
    }

    @GetMapping("/list")
    @Operation(summary = "TTS 리스트 조회",description = "TTS 저장 정보 리스트 조회")
    @ApiResponse(responseCode = "200",description = "리스트 조회 성공")
    public ResponseEntity<ResponseDTO> ttsList(@RequestParam(value = "page",defaultValue = "1")int page, @AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok().body(ttsService.getList(user,page-1));
    }
}
