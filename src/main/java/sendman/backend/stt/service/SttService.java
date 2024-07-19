package sendman.backend.stt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.account.domain.Account;
import sendman.backend.account.repository.AccountRepository;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.utils.AwsBucket;
import sendman.backend.stt.domain.Stt;
import sendman.backend.stt.repository.SttRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SttService {
    private final SttRepository sttRepository;
    private final AccountService accountService;
    private final AwsBucket bucket;

    public ResponseDTO saveVoice(User resUser, MultipartFile file) throws IOException {
        //AI API 통신 후 백업
        String text = " ";

        //AWS 파일 저장
        String url = bucket.saveFile(file,"voice");

        // 만료기간 설정 ( 7일 )
        LocalDate exp = LocalDate.now().plusWeeks(1);

        //db 저장
        Account account = accountService.findByAccount(resUser);
        sttRepository.save(Stt.builder()
                .url(url)
                .account(account)
                .exp(exp)
                .text(text)
                .build());

        return new ResponseDTO("저장되었습니다", null);
    }

    //paging 5개씩 데이터 전달
    public ResponseDTO getList(User user,int pageNum){
        Account account = accountService.findByAccount(user);
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Stt> sttPage = sttRepository.findByAccount(account,pageable);
        return new ResponseDTO(null, sttPage);
    }

    //매일 0시 5분마다 데이터 삭제
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    private void clearImageData(){
        List<Stt> voices = sttRepository.findByExp(LocalDate.now());

        sttRepository.deleteAll(voices);
    }
}
