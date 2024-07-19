package sendman.backend.stt.dto;

import sendman.backend.stt.domain.Stt;

import java.time.LocalDateTime;

public record SttResponseDTO(
        LocalDateTime createdDate,
        Long id,
        String text,
        String url
) {
    public SttResponseDTO(Stt stt){
        this(stt.getCreatedDate(), stt.getId(), stt.getText(), stt.getUrl());
    }
}
