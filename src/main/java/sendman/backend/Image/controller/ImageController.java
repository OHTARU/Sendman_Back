package sendman.backend.Image.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.Image.service.ImageService;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json", consumes = "multipart/form-data")
    public ResponseEntity<?> imageSave(@AuthenticationPrincipal User user,@RequestParam(value = "file")MultipartFile file){
        try {
            return ResponseEntity.created(URI.create("/image/save")).body(imageService.saveImage(user, file));
        }catch (IOException e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PostMapping("/stt")
    public ResponseEntity<?> sttSave(@AuthenticationPrincipal User user){
        return null;
    }

}
