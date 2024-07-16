package sendman.backend.voice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sendman.backend.voice.repository.VoiceRepository;

@Service
@RequiredArgsConstructor
public class VoiceService {
    private final VoiceRepository voiceRepository;

}
