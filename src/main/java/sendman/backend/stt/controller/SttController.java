package sendman.backend.stt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.stt.service.SttService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/stt")
@RequiredArgsConstructor
@Tag(name = "STT",description = "STT API 컨트롤러")
public class SttController {
    private final SttService sttService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    @Operation(summary = "STT 내용 저장",description = "STT 내용 저장 api")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "STT 내용 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 정보 오류"),
            @ApiResponse(responseCode = "500",description = "서버 내부 오류")
    })
    public ResponseEntity<ResponseDTO> sttSave(@AuthenticationPrincipal User user, @RequestParam(value = "file") MultipartFile file) {
        try {
            return ResponseEntity.created(URI.create("/sst/save")).body(sttService.saveVoice(user, file));
        } catch (RuntimeException | IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        }
    }

    @GetMapping("/list")
    @Operation(summary = "stt 리스트 조회",description = "stt 저장 정보 리스트 조회")
    @ApiResponse(responseCode = "200",description = "리스트 조회 성공")
    public ResponseEntity<ResponseDTO> sttList(@RequestParam(value = "page", defaultValue = "1") int page, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(sttService.getList(user, page - 1));
    }
}
