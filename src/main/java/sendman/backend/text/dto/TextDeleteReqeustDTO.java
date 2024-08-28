package sendman.backend.text.dto;

import java.util.List;

public record TextDeleteReqeustDTO(
        List<Long> id
) {
}
