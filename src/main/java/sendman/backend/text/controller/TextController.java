package sendman.backend.text.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.text.service.TextService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@Tag(name = "TEXT",description = "TEXT API 컨트롤러")
public class TextController {
    private final TextService textService;

    @RequestMapping(value = "/stt/save", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    @Operation(summary = "STT 내용 저장",description = "STT 내용 저장 api")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "STT 내용 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 정보 오류"),
            @ApiResponse(responseCode = "500",description = "서버 내부 오류")
    })
    public ResponseEntity<ResponseDTO> sttSave(@AuthenticationPrincipal User user, @RequestParam(value = "file") MultipartFile file) {
        try {
            return ResponseEntity.created(URI.create("/sst/save")).body(textService.saveVoice(user, file));
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        }
    }

    @PostMapping("/tts/save")
    @Operation(summary = "TTS 내용 저장", description = "TTS 내용 저장 API")
    @ApiResponses({
            @ApiResponse(responseCode = "201",description = "TTS 내용 저장 성공"),
            @ApiResponse(responseCode = "400", description = "요청 정보 오류"),
            @ApiResponse(responseCode = "500",description = "서버 내부 오류")
    })
    public ResponseEntity<ResponseDTO> ttsSave(@AuthenticationPrincipal User user,@RequestParam(value = "text")String text){
        try{
            return ResponseEntity.created(URI.create("/tts/save")).body(textService.saveImage(user, text));
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        }
    }

    @GetMapping("/list")
    @Operation(summary = "text 리스트 조회",description = "text 저장 정보 리스트 조회")
    @ApiResponse(responseCode = "200",description = "리스트 조회 성공")
    public ResponseEntity<ResponseDTO> textList(@RequestParam(value = "page",defaultValue = "1")int page, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok().body(textService.getList(user, page-1));
        }catch (UsernameNotFoundException e){
            return ResponseEntity.badRequest().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        }
    }
    @GetMapping("/detail")
    @Operation(summary = "text 상세 조회",description = "text 저장 정보 상세 조회")
    @ApiResponse(responseCode = "200",description = "리스트 조회 성공")
    @ApiResponse(responseCode = "404", description = "찾을 수 없는 페이지")
    public ResponseEntity<ResponseDTO> detail(@RequestParam(value = "id")Long id){
        try {
            return ResponseEntity.ok().body(textService.getDetail(id));
        }catch (ChangeSetPersister.NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }
}
