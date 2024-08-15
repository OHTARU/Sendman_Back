package sendman.backend.text.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sendman.backend.account.domain.Account;
import sendman.backend.account.service.AccountService;
import sendman.backend.common.dto.ResponseDTO;
import sendman.backend.common.utils.AwsBucket;
import sendman.backend.text.domain.SaveType;
import sendman.backend.text.domain.Text;
import sendman.backend.text.dto.AiResponseDTO;
import sendman.backend.text.dto.TextResponseDTO;
import sendman.backend.text.repository.TextRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TextService {
    private final TextRepository textRepository;
    private final AccountService accountService;
    private final AwsBucket bucket;
    @Value("${file.dir}")
    private String fileDir;

    public ResponseDTO saveVoice(User resUser, MultipartFile file) throws IOException, InterruptedException, UsernameNotFoundException {
        //AWS 파일 저장
        String url = bucket.saveFile(file,"voice");

        //AI API 통신 후 백업
        String text = aiGetText(file);

        // 만료기간 설정 ( 7일 )
        LocalDate exp = LocalDate.now().plusWeeks(1);

        //db 저장
        Account account = accountService.findByAccount(resUser);
        textRepository.save(Text.builder()
                .url(url)
                .account(account)
                .exp(exp)
                .text(text)
                .type(SaveType.STT)
                .build());

        return new ResponseDTO("저장되었습니다", new AiResponseDTO(text));
    }

    public ResponseDTO saveImage(User resUser, String text) throws UnsupportedEncodingException {
        Account account = accountService.findByAccount(resUser);

        //DB 저장
        textRepository.save(Text.builder()
                .text(text)
                .exp(LocalDate.now())
                .account(account)
                .type(SaveType.TTS)
                .build());

        return new ResponseDTO("저장 되었습니다.",null);
    }

    //매일 0시 5분마다 데이터 삭제
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
    public void clearImageData() throws IOException {
        List<Text> voices = textRepository.findByExp(LocalDate.now());
        for (Text t : voices){
            if(t.getType()==SaveType.STT){
                bucket.deleteFile("voice", t.getUrl());
            }else {
                //bucket.deleteFile("image", t.getUrl());
            }

        }
        textRepository.deleteAll(voices);
    }

    //paging 처리 5개씩 데이터 전송
    public ResponseDTO getList(User user,int pageNum) throws UsernameNotFoundException {
        Account account = accountService.findByAccount(user);
        Pageable pageable = PageRequest.of(pageNum, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<TextResponseDTO> textPage = textRepository.findByAccount(account,pageable).map(TextResponseDTO::new);

        return new ResponseDTO(pageNum + "번째 페이지 입니다.", textPage);
    }

    //detail Text 데이터 전송
    public ResponseDTO getDetail(Long id) throws ChangeSetPersister.NotFoundException {
        TextResponseDTO textResponseDTO = textRepository.findById(id).map(TextResponseDTO::new)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return new ResponseDTO(null, textResponseDTO);
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
        String savedPath = fileDir+ "voice/" + savedName;

        file.transferTo(new File(savedPath));

        ProcessBuilder builder = new ProcessBuilder(
                "src/main/resources/model/venv/bin/python3", "src/main/resources/model/Ko_STT.py",
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
            if (ch != '\n' && ch != '\r') {
                System.out.print((char) ch);
                sb.append((char) ch);
            }
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
