package sendman.backend.tts.dto;

import sendman.backend.tts.domain.Tts;

import java.time.LocalDateTime;
import java.util.Objects;

public record TtsResponseDTO(
        LocalDateTime createdDate,
        Long id,
        String text
) {
    public TtsResponseDTO(Tts tts){
        this(tts.getCreatedDate(), tts.getId(), tts.getText());
    }
}
