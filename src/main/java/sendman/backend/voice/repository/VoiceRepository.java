package sendman.backend.voice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sendman.backend.voice.domain.Voice;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {

}
