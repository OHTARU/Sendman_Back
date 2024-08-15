package sendman.backend.text.dto;

import sendman.backend.text.domain.Text;

import java.time.LocalDateTime;

public record TextResponseDTO(
        LocalDateTime createdDate,
        Long id,
        String text,
        String url,
        String type
) {
    public TextResponseDTO(Text text){
        this(text.getCreatedDate(), text.getId(), text.getText(), text.getUrl(), text.getText());
    }
}
