package sendman.backend.tts.controller;

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
public class TtsController {
    private final TtsService ttsService;

    @PostMapping("/save")
    public ResponseEntity<?> imageSave(@AuthenticationPrincipal User user,@RequestParam(value = "text")String text){
        return ResponseEntity.created(URI.create("/tts/save")).body(ttsService.saveText(user, text));
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> sttList(@RequestParam(value = "page",defaultValue = "1")int page, @AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok().body(ttsService.getList(user,page-1));
    }
}
