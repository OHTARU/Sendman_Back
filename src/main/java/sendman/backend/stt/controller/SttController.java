package sendman.backend.stt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.stt.service.SttService;
import sendman.backend.tts.service.TtsService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/stt")
@RequiredArgsConstructor
public class SttController {
    private final SttService sttService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<ResponseDTO> imageSave(@AuthenticationPrincipal User user, @RequestParam(value = "file") MultipartFile file){
        try {
            return ResponseEntity.created(URI.create("/sst/save")).body(sttService.saveVoice(user,file));
        }
        catch (RuntimeException | IOException e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(e.getMessage(), e.fillInStackTrace()));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> sttList(@RequestParam(value = "page",defaultValue = "1")int page,@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok().body(sttService.getList(user,page-1));
    }
}
