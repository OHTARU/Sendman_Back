package sendman.backend.tts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import sendman.backend.account.domain.Account;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.tts.domain.Tts;
import sendman.backend.tts.dto.TtsResponseDTO;
import sendman.backend.tts.repository.TtsRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TtsService {
    private final TtsRepository ttsRepository;
    private final AccountService accountService;

    public ResponseDTO saveText(User resUser, String text) {
        Account account = accountService.findByAccount(resUser);

        //DB 저장
        ttsRepository.save(Tts.builder()
                .text(text)
                .exp(LocalDate.now())
                .account(account)
                .build());

        return new ResponseDTO("저장 되었습니다.",null);
    }

    //paging 처리 5개씩 데이터 전송
    public ResponseDTO getList(User user,int pageNum){
        Account account = accountService.findByAccount(user);
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<TtsResponseDTO> sttPage = ttsRepository.findByAccount(account,pageable).map(TtsResponseDTO::new);

        return new ResponseDTO(null, sttPage);
    }

    //매일 0시 5분씩 데이터 삭제
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void clearImageData(){
        List<Tts> texts = ttsRepository.findByExp(LocalDate.now());

        ttsRepository.deleteAll(texts);
    }
}
