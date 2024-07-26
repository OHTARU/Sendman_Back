package sendman.backend.stt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.account.domain.Account;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.utils.AwsBucket;
import sendman.backend.stt.domain.Stt;
import sendman.backend.stt.dto.AiResponseDTO;
import sendman.backend.stt.repository.SttRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SttService {
    private final SttRepository sttRepository;
    private final AccountService accountService;
    private final AwsBucket bucket;
    @Value("${file.dir}")
    private String fileDir;

    public ResponseDTO saveVoice(User resUser, MultipartFile file) throws IOException, InterruptedException {
        //AWS 파일 저장
        String url = bucket.saveFile(file,"voice");

        //AI API 통신 후 백업
        String text = aiGetText(file);

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

        return new ResponseDTO("저장되었습니다", new AiResponseDTO(text));
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

    private String aiGetText(MultipartFile file) throws IOException, InterruptedException {
        String origName = file.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        file.transferTo(new File(savedPath));

        ProcessBuilder builder = new ProcessBuilder(
                "src/main/resources/model/myenv/bin/python3", "src/main/resources/model/text_print.py",
                "--model_path","src/main/resources/model/model_epoch9.pt",
                "--audio_path", savedPath);
        Process process = builder.start();

        InputStreamReader inputStream = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(inputStream);

        StringBuilder sb = new StringBuilder();
        String line;
        int ch;
        System.out.println("reader : ");
        while ((ch=reader.read())!=-1) {
            // 실행 결과 처리
            System.out.print((char)ch);
            sb.append((char)ch);
        }
        // 2. 프로세스 종료 대기
        int exitCode = process.waitFor();
        System.out.println("종료 코드: " + exitCode);

        // 3. 프로세스 강제 종료
        process.destroy();

        // 4. 종료 코드 확인
        int exitCode1 = process.exitValue();
        System.out.println("외부 프로그램 종료 코드: " + exitCode1);

        return sb.toString();
    }
}
